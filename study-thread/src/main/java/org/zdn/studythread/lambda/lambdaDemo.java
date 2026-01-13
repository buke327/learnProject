package org.zdn.studythread.lambda;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
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
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(new BigDecimal(10000), "CNY"));
        transactions.add(new Transaction(new BigDecimal(20000), "CNY"));
        transactions.add(new Transaction(new BigDecimal(30000), "CNY"));
        transactions.add(new Transaction(new BigDecimal(10000), "dollar"));
        transactions.add(new Transaction(new BigDecimal(20000), "dollar"));
        transactions.add(new Transaction(new BigDecimal(30000), "ZZZ"));
        transactions.add(new Transaction(new BigDecimal(20000), "ZZZ"));
        transactions.add(new Transaction(new BigDecimal(10000), "ZZZ"));

        transactions.stream()
                .filter(t -> t.getPrice().compareTo(BigDecimal.valueOf(10000)) > 0)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .map(Transaction::toString)
                                        .collect(Collectors.joining(",", "{", "}"))
                        )
                ))
                .forEach((currency, group) -> System.out.println(currency + ":" + group));

        /*Map<String, List<Transaction>> map = transactions.stream()
                .filter(t -> t.getPrice().intValue() > 10000)
                .collect(Collectors.groupingBy(Transaction::getCurrency));

        map.forEach((a, b) -> {
            StringBuilder stringBuilder = new StringBuilder(a);
            stringBuilder.append(":{");
            b.forEach(t -> {
                stringBuilder.append(t).append(",");
            });
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            stringBuilder.append("}");
            System.out.println(stringBuilder.toString());
        });*/
    }

    @Data
    static
    class Transaction {

        private BigDecimal price;

        private String currency;

        public Transaction(BigDecimal price, String currency) {
            this.price = price;
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "price=" + price +
                    ", currency='" + currency + '\'' +
                    '}';
        }
    }
}
