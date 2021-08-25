package ru.java.solid;

import java.util.List;

public interface Cell {

    void addBanknote(List<Nominal> banknotes);

    long getTotalAmount();

    int getCount();

    Nominal getBanknote();
}
