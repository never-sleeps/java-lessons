package ru.java.dataprocessor;

import ru.java.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    /**
     * Группирует выходящий список по name, при этом суммирует поля value
     * @param data - данные для обработки
     * @return обработанный список
     */
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                .collect(
                        Collectors.groupingBy(
                                Measurement::getName,
                                TreeMap::new,
                                Collectors.summingDouble(Measurement::getValue)
                        )
                );
    }
}
