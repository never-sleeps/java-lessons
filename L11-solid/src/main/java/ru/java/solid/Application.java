package ru.java.solid;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        final int VALUE_FOR_WITHDRAW = 8_660;

        Atm atm = new AtmImpl();
        atm.depositMoney(
                List.of(
                        Nominal.NOMINAL_5000,

                        Nominal.NOMINAL_2000,
                        Nominal.NOMINAL_2000,

                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,

                        Nominal.NOMINAL_500,

                        Nominal.NOMINAL_100,
                        Nominal.NOMINAL_100,
                        Nominal.NOMINAL_100,
                        Nominal.NOMINAL_100,
                        Nominal.NOMINAL_100,

                        Nominal.NOMINAL_50,
                        Nominal.NOMINAL_50,
                        Nominal.NOMINAL_50,

                        Nominal.NOMINAL_10
                )
        );

        System.out.println("total Amount = " + atm.getTotalAmount());
        Map<Nominal, Long> amountByNominal = atm.getAmountByNominal();
        sortByNominal(amountByNominal)
                .forEach((k, v) -> System.out.print(k + " : " + v / k.getValue() + " | "));

        System.out.print("\n\n");

        System.out.println("cash = " + VALUE_FOR_WITHDRAW);
        List<Nominal> cash = atm.withdrawMoney(VALUE_FOR_WITHDRAW);
        Map<Nominal, Long> cashByNominal = cash.stream()
                .collect(Collectors.groupingBy(Nominal::get, Collectors.counting()));
        sortByNominal(cashByNominal).forEach((k, v) -> {
            System.out.print(k + " : " + v + " | ");
        });

        System.out.print("\n\n");

        System.out.println("total Amount After Withdraw: " + atm.getTotalAmount());
        Map<Nominal, Long> amountByNominalAfterWithdraw = atm.getAmountByNominal();
        sortByNominal(amountByNominalAfterWithdraw)
                .forEach((k, v) -> System.out.print(k + " : " + v / k.getValue() + " | "));

        System.out.println();
    }

    private static Map<Nominal, Long> sortByNominal(Map<Nominal, Long> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new
                        ));
    }
}
