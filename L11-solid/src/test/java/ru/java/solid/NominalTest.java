package ru.java.solid;

import org.junit.jupiter.api.Test;
import ru.java.solid.exception.NotSupportedNominalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NominalTest {

    @Test
    void of_shouldThrowExceptionForNotSupportedNominal() {
        assertThatThrownBy( () -> Nominal.of(123))
                .isInstanceOf(NotSupportedNominalException.class)
                .hasMessage("Not supported nominal value: 123. Available denominations of banknotes: 5000, 2000, 1000, 500, 200, 100, 50, 10.");
    }

    @Test
    void of_shouldReturnNominal() {
        assertThat(Nominal.of(100)).isEqualTo(Nominal.NOMINAL_100);
    }
}