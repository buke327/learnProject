package org.zdn.studythread.threadnewdemo;

import org.zdn.studythread.vo.Student;

import java.util.concurrent.*;

public class NewThreadDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*Thread thread = new Thread(() -> {
            System.out.println("Thread start");
        });
        thread.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() {
                return "FutureTask Callable Start";
            }
        });
        Thread thread2 = new Thread(futureTask);
        thread2.start();
        System.out.println(futureTask.get());
        */

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        FutureTask<Student> futureTask1 = new FutureTask<>(new CallableDemo1());
        FutureTask<Student> futureTask2 = new FutureTask<>(new CallableDemo2());
        FutureTask<Student> futureTask3 = new FutureTask<>(new CallableDemo3());
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);
        executorService.submit(futureTask3);
        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get());
        System.out.println(futureTask1.get());
        executorService.shutdown();


    }
}
