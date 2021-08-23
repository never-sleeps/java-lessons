package ru.java.solid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        final int VALUE_FOR_WITHDRAW = 9_850;

        AtmImpl atm = new AtmImpl();
        atm.depositMoney(
                List.of(
                        new Banknote(5000),

                        new Banknote(2000),
                        new Banknote(2000),

                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(1000),

                        new Banknote(500),

                        new Banknote(200),
                        new Banknote(200),

                        new Banknote(100),
                        new Banknote(100),
                        new Banknote(100),

                        new Banknote(50),
                        new Banknote(50)
                )
        );
        System.out.println("total Amount = " + atm.getTotalAmount());
        Map<Nominal, Integer> amountByNominal = atm.getAmountByNominal();
        amountByNominal.forEach((k, v) -> System.out.print(k + " : " + v / k.getValue() + " | "));
        System.out.println();

        System.out.println("cash = " + VALUE_FOR_WITHDRAW);
        List<Banknote> cash = atm.withdrawMoney(VALUE_FOR_WITHDRAW);
        cash.stream()
                .collect(Collectors.groupingBy(Banknote::getNominal))
                .forEach((k, v) -> System.out.print(k + " : " + v.size() + " | "));
        System.out.println();

        System.out.println("amountByNominalAfterWithdraw: " + atm.getTotalAmount());
        Map<Nominal, Integer> amountByNominalAfterWithdraw = atm.getAmountByNominal();
        amountByNominalAfterWithdraw.forEach((k, v) -> System.out.print(k + " : " + v / k.getValue() + " | "));

        System.out.println();
    }
}
