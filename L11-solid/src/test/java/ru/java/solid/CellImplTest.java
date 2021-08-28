package ru.java.solid;

import org.junit.jupiter.api.Test;
import ru.java.solid.exception.NotSupportedNominalByCellException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CellImplTest {

    @Test
    void constructor_shouldAddNominalListToCell() {
        // given
        List<Nominal> banknotes = List.of(
                Nominal.NOMINAL_500,
                Nominal.NOMINAL_500
        );
        long totalAmount = banknotes.stream().mapToLong(Nominal::getValue).sum();

        // when
        Cell cell = new CellImpl(Nominal.NOMINAL_500.getValue(), banknotes);

        // then
        assertThat(cell.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(cell.getCount()).isEqualTo(banknotes.size());
    }

    @Test
    void addBanknote_shouldAddNominalListToCell() {
        // given
        List<Nominal> banknotes = List.of(
                Nominal.NOMINAL_500,
                Nominal.NOMINAL_500
        );
        List<Nominal> banknotesForAdding = List.of(
                Nominal.NOMINAL_500
        );
        long totalAmount = banknotes.stream().mapToLong(Nominal::getValue).sum() +
                banknotesForAdding.stream().mapToLong(Nominal::getValue).sum();
        Cell cell = new CellImpl(Nominal.NOMINAL_500.getValue(), banknotes);

        // when
        cell.addBanknote(banknotesForAdding);

        // then
        assertThat(cell.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(cell.getCount()).isEqualTo(banknotes.size() + banknotesForAdding.size());
    }

    @Test
    void addBanknote_shouldThrowExceptionForNotSupportedNominalByCell() {
        // given
        List<Nominal> banknotes = List.of(
                Nominal.NOMINAL_500,
                Nominal.NOMINAL_500
        );
        List<Nominal> banknotesForAdding = List.of(
                Nominal.NOMINAL_100
        );
        long totalAmount = banknotes.stream().mapToLong(Nominal::getValue).sum();
        Cell cell = new CellImpl(Nominal.NOMINAL_500.getValue(), banknotes);

        // when then
        assertThatThrownBy( () -> cell.addBanknote(banknotesForAdding))
                .isInstanceOf(NotSupportedNominalByCellException.class)
                .hasMessage("Not supported nominal '100' by cell. Available nominal: '500'.");

        assertThat(cell.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(cell.getCount()).isEqualTo(banknotes.size());
    }
}