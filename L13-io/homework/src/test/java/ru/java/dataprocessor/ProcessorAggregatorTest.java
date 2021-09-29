package ru.java.dataprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.java.model.Measurement;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;

class ProcessorAggregatorTest {

    private final ProcessorAggregator processorAggregator = new ProcessorAggregator();

    @Test
    @DisplayName("Данные должны группироваться по name и сортироваться по ключу, поля value должны суммироваться")
    void processShouldGroupAndAggregateData() {
        // given
        List<Measurement> list = Arrays.asList(
                new Measurement("z", 10),
                new Measurement("y", 20),
                new Measurement("x", 30),
                new Measurement("x", 40)
        );

        // when
        var result = processorAggregator.process(list);

        // then
        Assertions.assertEquals(new AbstractMap.SimpleEntry<>("x", 70d), result.entrySet().iterator().next());
        Assertions.assertEquals(3, result.size());
    }
}