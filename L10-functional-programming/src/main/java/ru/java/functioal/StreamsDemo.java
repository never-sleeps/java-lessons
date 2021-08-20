package ru.java.functioal;

import java.util.*;
import java.util.stream.Collectors;

public class StreamsDemo {
    public static void main(String[] args) {
        List<Computer> list = List.of(
                new Computer("Apple", "MacBook Pro", 3600),
                new Computer("ASUS", " ZenBook Pro", 2499),
                new Computer("Acer", "aspire 3", 1699),
                new Computer("Apple", "MacBook Air", 2100)
        );


        Map<Boolean, List<Computer>> computers1 = list.stream().collect(Collectors.partitioningBy(c -> c.getPrice() > 2000));
        printResultWithPartitioningBy(computers1);
        /*
            <= 2000: [Computer{company='Acer', model='aspire 3', price=1699}]
            > 2000: [Computer{company='Apple', model='MacBook Pro', price=3600}, Computer{company='ASUS', model=' ZenBook Pro', price=2499}, Computer{company='Apple', model='MacBook Air', price=2100}]
         */

        // группировка данных по типу
        Map<String, List<Computer>> computers2 = list.stream().collect(Collectors.groupingBy(Computer::getCompany));
        printResultWithGroupingByType(computers2);
        /*
            Model = Acer
            Computer{company='Acer', model='aspire 3', price=1699}
            Model = Apple
            Computer{company='Apple', model='MacBook Pro', price=3600}
            Computer{company='Apple', model='MacBook Air', price=2100}
            Model = ASUS
            Computer{company='ASUS', model=' ZenBook Pro', price=2499}
        */

        // подсчёт элементов потока данных заданного типа.
        // Посчитаем количество производимых компьютеров в каждой компании
        Map<String, Long> computers3 = list.stream().collect(Collectors.groupingBy(
                Computer::getCompany,
                Collectors.counting())
        );
        printResult(computers3);
        /*
            Acer = 1
            Apple = 2
            ASUS = 1
         */

        // общая стоимость всех компьютеров в зависимости от типа компьютера
        Map<String, Integer> computers4 = list.stream().collect(Collectors.groupingBy(
                Computer::getCompany,
                Collectors.summingInt(Computer::getPrice))
        );
        printResult(computers4);
        /*
            Acer = 1699
            Apple = 5700
            ASUS = 2499
         */

        Map<String, IntSummaryStatistics> computers5 = list.stream().collect(Collectors.groupingBy(
                Computer::getCompany,
                Collectors.summarizingInt(Computer::getPrice))
        );
        printResultWithIntSummaryStatistics(computers5);
        /*
            Value of Acer: 1
            Min price of Acer: 1699
            Max price of Acer: 1699
            Avg price of Acer: 1699.0
            Total cost of Acer: 1699

            Value of Apple: 2
            Min price of Apple: 2100
            Max price of Apple: 3600
            Avg price of Apple: 2850.0
            Total cost of Apple: 5700

            Value of ASUS: 1
            Min price of ASUS: 2499
            Max price of ASUS: 2499
            Avg price of ASUS: 2499.0
            Total cost of ASUS: 2499
         */
    }

    private static void printResultWithPartitioningBy(Map<Boolean, List<Computer>> computers1) {
        for(Map.Entry<Boolean, List<Computer>> item : computers1.entrySet()) {
            if(item.getKey()) {
                System.out.println("> 2000: " + item.getValue());
            } else {
                System.out.println("<= 2000: " + item.getValue());
            }
        }
    }

    private static void printResultWithGroupingByType(Map<String, List<Computer>> computers2) {
        for(Map.Entry<String, List<Computer>> item : computers2.entrySet()) {
            System.out.println("Model = " + item.getKey());
            for (Computer c : item.getValue()) {
                System.out.println(c);
            }
        }
    }

    private static void printResult(Map<String, ?> computers3) {
        for(Map.Entry<String, ?> item : computers3.entrySet()) {
            System.out.println(item.getKey() + " = " + item.getValue());
        }
    }

    private static void printResultWithIntSummaryStatistics(Map<String, IntSummaryStatistics> computers) {
        for(Map.Entry<String, IntSummaryStatistics> item : computers.entrySet()) {
            System.out.println(String.format("Value of %s: %d", item.getKey(), item.getValue().getCount()));
            System.out.println(String.format("Min price of %s: %d", item.getKey(), item.getValue().getMin()));
            System.out.println(String.format("Max price of %s: %d", item.getKey(), item.getValue().getMax()));
            System.out.println(String.format("Avg price of %s: %s", item.getKey(), item.getValue().getAverage()));
            System.out.println(String.format("Total cost of %s: %d", item.getKey(), item.getValue().getSum()));
            System.out.println();
        }
    }

    private static class Computer {
        private String company;
        private String model;
        private int price;

        public Computer(String company, String model, int price) {
            this.company = company;
            this.model = model;
            this.price = price;
        }

        public String getCompany() {
            return company;
        }

        public String getModel() {
            return model;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Computer{" +
                    "company='" + company + '\'' +
                    ", model='" + model + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
}
