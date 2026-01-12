package org.zdn.studythread.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class lambdaDemo {

    public static void main(String[] args) {
        /*List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter + 谓词
        List<String> longNames = names.stream()
                .filter(name -> name.length() > 4)
                .toList();
        System.out.println("Long names: " + longNames);

        Optional<Integer> sum = numbers.stream()
                .reduce(Integer::sum);
        System.out.println("Sum: " + sum.get());*/
        demonstrateParallelStreams();
    }

    public static void demonstrateParallelStreams() {
        List<Integer> numbers = IntStream.rangeClosed(1, 1_000_000)
                .boxed()
                .toList();

        // 串行流
        long serialStart = System.currentTimeMillis();
        long serialCount = numbers.stream()
                .filter(n -> n % 2 == 0)
                .count();
        long serialTime = System.currentTimeMillis() - serialStart;

        // 并行流
        long parallelStart = System.currentTimeMillis();
        long parallelCount = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .count();
        long parallelTime = System.currentTimeMillis() - parallelStart;

        System.out.println("Serial: " + serialCount + " in " + serialTime + "ms");
        System.out.println("Parallel: " + parallelCount + " in " + parallelTime + "ms");

        // 注意：并行流中的Lambda应该是无状态的，线程安全的
        List<Integer> unsafeOperation = numbers.parallelStream()
                .map(n -> {
                    // ❌ 危险：共享可变状态
                    // sharedCounter++;
                    return n * 2;
                })
                .toList();
    }
}
