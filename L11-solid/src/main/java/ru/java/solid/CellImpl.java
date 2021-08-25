package ru.java.solid;

import ru.java.solid.exception.NotEnoughMoneyException;
import ru.java.solid.exception.NotSupportedNominalByCellException;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {

    private final List<Nominal> banknotes = new ArrayList<>();

    public CellImpl(List<Nominal> banknotes) {
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
                Nominal currentNominal = this.banknotes.stream().findAny().get();
                if (nominal.getValue() != currentNominal.getValue()) {
                    throw new NotSupportedNominalByCellException(nominal, currentNominal);
                }
            }
        }
    }
}
