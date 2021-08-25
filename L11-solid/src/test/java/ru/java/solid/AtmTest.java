package ru.java.solid;

import org.junit.jupiter.api.Test;
import ru.java.solid.exception.MoneyDistributionException;
import ru.java.solid.exception.NotEnoughMoneyException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AtmTest {

    private final Atm atm = new AtmImpl();

    @Test
    void getTotalAmount_shouldReturnTotalAmount() {
        assertThat(atm.getTotalAmount()).isEqualTo(0);
    }

    @Test
    void depositMoney_shouldDepositMoneyToAccount() {
        // given
        atm.depositMoney(
                List.of(
                        Nominal.NOMINAL_5000,

                        Nominal.NOMINAL_2000,
                        Nominal.NOMINAL_2000,

                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,

                        Nominal.NOMINAL_500,

                        Nominal.NOMINAL_200,
                        Nominal.NOMINAL_200,

                        Nominal.NOMINAL_100,

                        Nominal.NOMINAL_50,
                        Nominal.NOMINAL_50
                )
        );

        // when then
        assertThat(atm.getAmountByNominal())
                .containsOnly(
                        entry(Nominal.NOMINAL_5000, 5000L),
                        entry(Nominal.NOMINAL_2000, 4000L),
                        entry(Nominal.NOMINAL_1000, 3000L),
                        entry(Nominal.NOMINAL_500, 500L),
                        entry(Nominal.NOMINAL_200, 400L),
                        entry(Nominal.NOMINAL_100, 100L),
                        entry(Nominal.NOMINAL_50, 100L)
                );
        assertThat(atm.getTotalAmount()).isEqualTo(13_100);
    }

    @Test
    void withdrawMoney_shouldWithdrawMoneyAndReturnNominalList() {
        // given
        atm.depositMoney(
                List.of(
                        Nominal.NOMINAL_5000,

                        Nominal.NOMINAL_2000,
                        Nominal.NOMINAL_2000,

                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,
                        Nominal.NOMINAL_1000,

                        Nominal.NOMINAL_500,

                        Nominal.NOMINAL_200,
                        Nominal.NOMINAL_200,

                        Nominal.NOMINAL_100,

                        Nominal.NOMINAL_50,
                        Nominal.NOMINAL_50
                )
        );

        // when
        List<Nominal> banknotes = atm.withdrawMoney(7750);

        // then
        assertThat(banknotes).hasSize(5);
        assertThat(atm.getTotalAmount()).isEqualTo(5_350);
        assertThat(atm.getAmountByNominal())
                .containsOnly(
                        entry(Nominal.NOMINAL_5000, 0L),
                        entry(Nominal.NOMINAL_2000, 2_000L),
                        entry(Nominal.NOMINAL_1000, 3_000L),
                        entry(Nominal.NOMINAL_500, 0L),
                        entry(Nominal.NOMINAL_200, 200L),
                        entry(Nominal.NOMINAL_100, 100L),
                        entry(Nominal.NOMINAL_50, 50L)
                );
    }

    @Test
    void withdrawMoney_shouldThrowExceptionForMoneyDistributionError() {
        // given
        atm.depositMoney(List.of(Nominal.NOMINAL_5000));

        // when then
        assertThatThrownBy( () -> atm.withdrawMoney(123))
                .isInstanceOf(MoneyDistributionException.class)
                .hasMessage("Distribution error. Try to request a another amount");

        assertThat(atm.getTotalAmount()).isEqualTo(5_000);
    }

    @Test
    void withdrawMoney_shouldThrowExceptionIfNotEnoughMoney() {
        assertThatThrownBy( () -> atm.withdrawMoney(100))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessage("Not enough money: requestedAmount 100 is greater than the available amount 0");
    }
}
