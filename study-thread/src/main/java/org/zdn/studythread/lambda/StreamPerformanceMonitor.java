package org.zdn.studythread.lambda;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamPerformanceMonitor {

    public static void main(String[] args) {
        monitorStreamPerformance();
    }

    public static void monitorStreamPerformance() {
        List<Integer> data = IntStream.rangeClosed(1, 2_000_000)
                .boxed().collect(Collectors.toList());

        // 监控并行流的线程使用情况
        System.out.println("=== 并行流线程使用监控 ===");

        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger maxActiveThreads = new AtomicInteger(0);
        AtomicInteger totalTasks = new AtomicInteger(0);

        // 使用自定义ForkJoinPool来监控
        ForkJoinPool customPool = new ForkJoinPool(
                Runtime.getRuntime().availableProcessors(),
                new MonitoringForkJoinWorkerThreadFactory(maxActiveThreads, totalTasks),
                null, true);

        try {
            customPool.submit(() -> {
                latch.countDown();
                List<Integer> result = data.parallelStream()
                        .filter(n -> n % 2 == 0)
                        .map(n -> n * n)
                        .collect(Collectors.toList());
            }).get();

            System.out.println("最大活跃线程数: " + maxActiveThreads.get());
            System.out.println("总任务数: " + totalTasks.get());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customPool.shutdown();
        }
    }

    static class MonitoringForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        private final AtomicInteger maxActiveThreads;
        private final AtomicInteger totalTasks;
        private final AtomicInteger createdThreads = new AtomicInteger(0);

        public MonitoringForkJoinWorkerThreadFactory(AtomicInteger maxActive, AtomicInteger total) {
            this.maxActiveThreads = maxActive;
            this.totalTasks = total;
        }

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            ForkJoinWorkerThread thread = new MonitoringForkJoinWorkerThread(pool);
            thread.setName("monitoring-thread-" + createdThreads.incrementAndGet());
            return thread;
        }

        class MonitoringForkJoinWorkerThread extends ForkJoinWorkerThread {
            public MonitoringForkJoinWorkerThread(ForkJoinPool pool) {
                super(pool);
                maxActiveThreads.set(Math.max(maxActiveThreads.get(), pool.getActiveThreadCount()));
            }

            @Override
            protected void onStart() {
                super.onStart();
                totalTasks.incrementAndGet();
            }
        }
    }
}