package ru.java.solid;

import ru.java.solid.exception.MoneyDistributionException;
import ru.java.solid.exception.NotEnoughMoneyException;

import java.util.*;
import java.util.stream.Collectors;

public class AtmImpl implements Atm{

    private final Map<Nominal, Cell> storage = new EnumMap<>(Nominal.class);

    public AtmImpl() {}

    /**
     * Внесение банкнот в банкомат в отдельную ячейку для каждого номинала.
     * @param banknotesForDeposit - список банкнот
     */
    @Override
    public void depositMoney(List<Banknote> banknotesForDeposit) {
        // группируем в списки по номиналу
        Map<Nominal, List<Banknote>> byNominal = banknotesForDeposit.stream()
                .collect(Collectors.groupingBy(Banknote::getNominal));
        // распределяем по ячейкам
        byNominal.forEach(
                (nominal, banknotes) -> {
                    storage.compute(
                            nominal, (k, cell) -> {
                                if (cell == null) {
                                    return new Cell(banknotes);
                                } else {
                                    cell.addBanknote(banknotes);
                                    return cell;
                                }
                            });
                }
        );
    }

    /**
     * Выдача запрошенной суммы минимальным количеством банкнот.
     * @param requestedAmount - запрошенная сумма
     * @throws NotEnoughMoneyException, если сумму нельзя выдать
     * @return список банкнот
     */
    @Override
    public List<Banknote> withdrawMoney(int requestedAmount) {
        int availableAmount = getTotalAmount();
        if (availableAmount < requestedAmount) {
            throw new NotEnoughMoneyException(requestedAmount, availableAmount);
        }
        List<Nominal> nominalList = splitAmountByNominal(requestedAmount).entrySet().stream()
                .map(entry -> Collections.nCopies(entry.getValue(), entry.getKey()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return nominalList.stream()
                .map(it -> storage.get(it).getBanknote())
                .collect(Collectors.toList());
    }

    /**
     * Информация о сумме для каждого номинала на основе алгоритм расчета выдаваемого количества банкнот.
     * @param requestedAmount запрашиваемая сумма
     * @return Информация о сумме для каждого номинала
     */
    private Map<Nominal, Integer> splitAmountByNominal(int requestedAmount) {
        final Integer[] notes = getNotes();
        List<Integer> distribution = getDistributionForRequestedAmount(requestedAmount);
        Map<Nominal, Integer> amountByNominal = new LinkedHashMap<>();
        for (int i = 0; i < notes.length - 1; i++) {
            amountByNominal.put(Nominal.of(notes[i]), distribution.get(i));
        }
        return amountByNominal;
    }

    /**
     * Информация о сумме остатка денежных средств
     * @return сумма остатка денежных средств
     */
    @Override
    public int getTotalAmount() {
        return storage.values().stream()
                .mapToInt(Cell::getAmount)
                .sum();
    }

    /**
     * @return Информация о сумме для каждого номинала
     */
    @Override
    public Map<Nominal, Integer> getAmountByNominal() {
        return storage.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAmount()));
    }

    /**
     * Определение идеального распределения:
     * 8_850 = 1 * 5000 + 1 * 2000 + 1 * 1000 + 1 * 500 + 1 * 200 + 1 * 100 + 1 * 50.
     * @return идеальное распределение
     */
    public List<Integer> getIdealDistribution(double amount) {
        Integer[] notes = getNotes();

        List<Integer> idealDistribution = new ArrayList<>();
        for(int i = 0 ; i < storage.size() ; i++){
            int nominal = Nominal.of(notes[i]).getValue();
            double remainder = amount % nominal;
            idealDistribution.add((int) ((amount - remainder) / nominal));
            amount = remainder;
        }
        if (amount != 0) { // нет номинала для остатка
            throw new MoneyDistributionException();
        }
        return idealDistribution;
    }

    private Integer[] getNotes() {
        return Arrays.stream(Nominal.values())
                .map(Nominal::getValue)
                .sorted(Collections.reverseOrder())
                .toArray(Integer[]::new);
    }

    /**
     * Алгоритм расчета выдаваемого количества банкнот в два прохода.
     *
     * Первый проход - определение идеального распределения:
     * 8_850 = 1 * 5000 + 1 * 2000 + 1 * 1000 + 1 * 500 + 1 * 200 + 1 * 100 + 1 * 50.
     *
     * Второй проход - получение реального из идеального распределения:
     * Купюры анализируются по убыванию из номинала.
     * При недостаточном количестве купюр снимаются имеющиеся,
     * а при анализе купюр следующего номинала добавляется остаток от предыдущего анализа.
     *
     * @param amount запрашиваемая сумма
     * @return необходимое на выдачу количество банкнот каждого номинала или null, если распределение невозможно
     */
    public List<Integer> getDistributionForRequestedAmount(double amount) {
        final Integer[] notes = getNotes();
        List<Integer> idealDistribution = getIdealDistribution(amount);

        List<Integer> distributionReal = new ArrayList<>();
        int remain = 0;

        // алгоритм для всех банкнот, кроме последней
        for (int i = 0; i < storage.size() - 1; i++) {
            Nominal nominal = Nominal.of(notes[i]);
            if (idealDistribution.get(i) + remain <= storage.get(nominal).getCount()) {
                distributionReal.add(idealDistribution.get(i) + remain);
                remain = 0;
            } else {
                distributionReal.add(storage.get(nominal).getCount());
                remain += idealDistribution.get(i) - storage.get(nominal).getCount();
                remain = remain * (nominal.getValue() / Nominal.of(notes[i + 1]).getValue());
            }
        }
        // тот же алгоритм для последней банкноты
        int i = storage.size() - 1;
        if (idealDistribution.get(i) + remain <= storage.get(Nominal.of(notes[i])).getCount()) {
            distributionReal.add(idealDistribution.get(i) + remain);
        } else {
            throw new MoneyDistributionException();
        }
        return distributionReal;
    }
}