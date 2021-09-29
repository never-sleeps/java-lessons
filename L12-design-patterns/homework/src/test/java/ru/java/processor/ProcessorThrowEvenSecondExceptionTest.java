package ru.java.processor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.java.exception.EvenSecondException;
import ru.java.model.Message;

import java.time.LocalDateTime;

class ProcessorThrowEvenSecondExceptionTest {

    private final DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);
    private final ProcessorThrowEvenSecondException processor = new ProcessorThrowEvenSecondException(dateTimeProvider);

    @Test
    @DisplayName("Процессор выбрасывает исключение на чётной секунде")
    void shouldThrowsExceptionForEvenSecond() {
        // given
        var message = new Message.Builder(1L).build();
        int evenSecond = 0;
        LocalDateTime currentTime = LocalDateTime.of(2020, 1, 1, 0, 0, evenSecond);
        Mockito.when(dateTimeProvider.getDataTime()).thenReturn(currentTime);

        // when // then
        Assertions.assertThatExceptionOfType(EvenSecondException.class).isThrownBy(
                () -> processor.process(message));
    }

    @Test
    @DisplayName("Процессор не выбрасывает исключение на нечётной секунде")
    void shouldReturnMessageForOddSecond() {
        // given
        var message = new Message.Builder(1L).build();
        int oddSecond = 1;
        LocalDateTime currentTime = LocalDateTime.of(2020, 1, 1, 0, 0, oddSecond);
        Mockito.when(dateTimeProvider.getDataTime()).thenReturn(currentTime);

        // when // then
        Assertions.assertThat(processor.process(message)).isNotNull();
    }
}