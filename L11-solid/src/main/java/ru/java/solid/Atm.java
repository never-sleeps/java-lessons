package ru.java.solid;

import java.util.List;
import java.util.Map;

public interface Atm {

    void depositMoney(List<Banknote> banknotesForDeposit);

    List<Banknote> withdrawMoney(int amount);

    int getTotalAmount();

    Map<Nominal, Integer> getAmountByNominal();
}
