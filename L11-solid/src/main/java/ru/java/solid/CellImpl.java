package ru.java.solid;

import ru.java.solid.exception.NotEnoughMoneyException;
import ru.java.solid.exception.NotSupportedNominalByCellException;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {

    private final int currentNominal;
    private final List<Nominal> banknotes = new ArrayList<>();

    public CellImpl(int nominal, List<Nominal> banknotes) {
        this.currentNominal = nominal;
        checkNominal(banknotes);
        this.banknotes.addAll(banknotes);
    }

    @Override
    public void addBanknote(List<Nominal> banknotes) {
        checkNominal(banknotes);
        this.banknotes.addAll(banknotes);
    }

    @Override
    public long getTotalAmount() {
        return banknotes.stream()
                .mapToLong(Nominal::getValue)
                .sum();
    }

    @Override
    public int getCount() {
        return banknotes.size();
    }

    @Override
    public Nominal getBanknote() {
        if (banknotes.isEmpty()) {
            throw new NotEnoughMoneyException();
        }
        int idx = banknotes.size() - 1;
        Nominal banknote = banknotes.get(idx);
        banknotes.remove(banknote);
        return banknote;
    }

    /**
     * Проверка добавления в ячейку банкнот только одного номинала
     * @param banknotes - добавляемые банкноты
     * @throws NotSupportedNominalByCellException - если номиналы отличаются
     */
    private void checkNominal(List<Nominal> banknotes) {
        if (!banknotes.isEmpty()) {
            for (Nominal nominal : banknotes) {
                if (nominal.getValue() != this.currentNominal) {
                    throw new NotSupportedNominalByCellException(nominal, Nominal.of(this.currentNominal));
                }
            }
        }
    }
}
