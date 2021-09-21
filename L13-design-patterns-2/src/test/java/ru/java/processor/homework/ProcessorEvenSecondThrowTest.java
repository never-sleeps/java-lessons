package ru.java.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.java.exception.EvenSecondException;
import ru.java.model.Message;
import ru.java.processor.ProcessorEvenSecondThrow;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProcessorEvenSecondThrowTest {

    private final LocalDateTime currentDateTime = Mockito.mock(LocalDateTime.class);
    private final ProcessorEvenSecondThrow processorEvenSecondThrow = new ProcessorEvenSecondThrow(currentDateTime);

    @Test
    @DisplayName("Процессор выбрасывает исключение на чётной секунде")
    void shouldThrowsExceptionForEvenSecond() {
        // given
        var message = new Message.Builder(1L).build();
        int evenSecond = 0;
        Mockito.when(currentDateTime.getSecond()).thenReturn(evenSecond);

        // when // then
        assertThatExceptionOfType(EvenSecondException.class).isThrownBy(
                () -> processorEvenSecondThrow.process(message));
    }

    @Test
    @DisplayName("Процессор не выбрасывает исключение на нечётной секунде")
    void shouldReturnMessageForOddSecond() {
        // given
        var message = new Message.Builder(1L).build();
        int oddSecond = 1;
        Mockito.when(currentDateTime.getSecond()).thenReturn(oddSecond);

        // when // then
        Assertions.assertThat(processorEvenSecondThrow.process(message)).isNotNull();
    }
}