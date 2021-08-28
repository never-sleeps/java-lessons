package ru.java.solid;

import ru.java.solid.exception.MoneyDistributionException;
import ru.java.solid.exception.NotEnoughMoneyException;

import java.util.*;
import java.util.stream.Collectors;

public class AtmImpl implements Atm{

    private final Map<Nominal, Cell> storage;

    public AtmImpl() {
        storage = new EnumMap<>(Nominal.class);
    }

    /**
     * Внесение банкнот в банкомат в отдельную ячейку для каждого номинала.
     * @param banknotesForDeposit - список банкнот
     */
    @Override
    public void depositMoney(List<Nominal> banknotesForDeposit) {
        // группируем в списки по номиналу
        Map<Nominal, List<Nominal>> byNominal = banknotesForDeposit.stream()
                .collect(Collectors.groupingBy(Nominal::get));
        // распределяем по ячейкам
        getAllNominals().forEach(
                nominal -> {
                    storage.compute(
                            nominal, (k, cell) -> {
                                if (cell == null) {
                                    return new CellImpl(
                                            nominal.getValue(),
                                            byNominal.getOrDefault(nominal, Collections.emptyList())
                                    );
                                } else {
                                    cell.addBanknote(byNominal.get(nominal));
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
    public List<Nominal> withdrawMoney(long requestedAmount) {
        long availableAmount = getTotalAmount();
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
     * @return Информация о сумме для каждого номинала [Nominal, count]
     */
    private Map<Nominal, Integer> splitAmountByNominal(long requestedAmount) {
        return getDistributionForRequestedAmount(requestedAmount);
    }

    /**
     * Информация о сумме остатка денежных средств
     * @return сумма остатка денежных средств
     */
    @Override
    public long getTotalAmount() {
        return storage.values().stream()
                .mapToLong(Cell::getTotalAmount)
                .sum();
    }

    /**
     * @return Информация о сумме для каждого номинала
     */
    @Override
    public Map<Nominal, Long> getAmountByNominal() {
        return storage.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getTotalAmount()));
    }

    /**
     * Определение идеального распределения:
     * 8_850 = 1 * 5000 + 1 * 2000 + 1 * 1000 + 1 * 500 + 1 * 200 + 1 * 100 + 1 * 50.
     * @return идеальное распределение
     */
    private Map<Nominal, Integer> getIdealDistribution(double amount) {
        final List<Nominal> nominals = getAllNominals();

        Map<Nominal, Integer> idealDistribution = new TreeMap<>();
        for(int i = 0 ; i < nominals.size() ; i++){
            int nominal = nominals.get(i).getValue();
            double remainder = amount % nominal;
            idealDistribution.put(Nominal.of(nominal), (int) ((amount - remainder) / nominal));
            amount = remainder;
        }
        if (amount != 0) { // нет номинала для остатка
            throw new MoneyDistributionException();
        }
        return idealDistribution;
    }

    /**
     * @return список доступных в приложении номиналов
     */
    private List<Nominal> getAllNominals() {
        return Arrays.stream(Nominal.values())
                .map(Nominal::get)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
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
    private Map<Nominal, Integer> getDistributionForRequestedAmount(double amount) {
        final List<Nominal> nominals = getAllNominals();
        Map<Nominal, Integer> idealDistribution = getIdealDistribution(amount);
        Map<Nominal, Integer> distributionReal = new TreeMap<>();
        int remain = 0;

        // алгоритм для всех банкнот, кроме последней
        for (int i = 0; i < nominals.size() - 1; i++) {
            Nominal nominal = nominals.get(i);
            int count = (storage.containsKey(nominal)) ? storage.get(nominal).getCount() : 0;
            if (idealDistribution.get(nominal) + remain <= count) {
                distributionReal.put(nominal, idealDistribution.get(nominal) + remain);
                remain = 0;
            } else {
                distributionReal.put(nominal, count);
                remain += idealDistribution.get(nominal) - count;
                remain = remain * (nominal.getValue() / nominals.get(i + 1).getValue());
            }
        }
        // тот же алгоритм для последней банкноты
        int i = nominals.size() - 1;
        Nominal nominal = nominals.get(i);
        int count = (storage.containsKey(nominal)) ? storage.get(nominal).getCount() : 0;
        if (idealDistribution.get(nominal) + remain <= count) {
            distributionReal.put(nominal, idealDistribution.get(nominal) + remain);
        } else {
            throw new MoneyDistributionException();
        }
        return distributionReal;
    }
}