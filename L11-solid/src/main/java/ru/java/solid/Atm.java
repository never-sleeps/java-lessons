package ru.java.solid;

import java.util.List;
import java.util.Map;

public interface Atm {

    void depositMoney(List<Nominal> banknotesForDeposit);

    List<Nominal> withdrawMoney(long requestedAmount);

    long getTotalAmount();

    Map<Nominal, Long> getAmountByNominal();
}
