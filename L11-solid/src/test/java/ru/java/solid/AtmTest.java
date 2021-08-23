package ru.java.solid;

import org.junit.jupiter.api.Test;
import ru.java.solid.exception.NotEnoughMoneyException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AtmTest {

    private final AtmImpl atm = new AtmImpl();

    @Test
    void getTotalAmount_ShouldReturnTotalAmount() {
        assertThat(atm.getTotalAmount()).isEqualTo(0);
    }

    @Test
    void depositMoney_shouldDepositMoneyToAccount() {
        atm.depositMoney(
                List.of(
                        new Banknote(5000),

                        new Banknote(2000),
                        new Banknote(2000),

                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(1000),

                        new Banknote(500),

                        new Banknote(200),
                        new Banknote(200),

                        new Banknote(100),

                        new Banknote(50),
                        new Banknote(50)
                )
        );
        assertThat(atm.getAmountByNominal())
                .containsOnly(
                        entry(Nominal.NOMINAL_5000, 5000),
                        entry(Nominal.NOMINAL_2000, 4000),
                        entry(Nominal.NOMINAL_1000, 3000),
                        entry(Nominal.NOMINAL_500, 500),
                        entry(Nominal.NOMINAL_200, 400),
                        entry(Nominal.NOMINAL_100, 100),
                        entry(Nominal.NOMINAL_50, 100)
                );
        assertThat(atm.getTotalAmount()).isEqualTo(13_100);
    }

    @Test
    void withdrawMoney_shouldWithdrawMoney() {
        atm.depositMoney(
                List.of(
                        new Banknote(5000),

                        new Banknote(2000),
                        new Banknote(2000),

                        new Banknote(1000),
                        new Banknote(1000),
                        new Banknote(1000),

                        new Banknote(500),

                        new Banknote(200),
                        new Banknote(200),

                        new Banknote(100),

                        new Banknote(50),
                        new Banknote(50)
                )
        );

        List<Banknote> banknotes = atm.withdrawMoney(7750);
        assertThat(banknotes).hasSize(5);

        assertThat(banknotes)
                .extracting("nominal", "value")
                .contains(
                        tuple(Nominal.NOMINAL_5000, 5_000),
                        tuple(Nominal.NOMINAL_2000, 2_000),
                        tuple(Nominal.NOMINAL_500, 500),
                        tuple(Nominal.NOMINAL_200, 200),
                        tuple(Nominal.NOMINAL_50, 50)
                );

        assertThat(atm.getTotalAmount()).isEqualTo(5_350);

        assertThat(atm.getAmountByNominal())
                .containsOnly(
                        entry(Nominal.NOMINAL_5000, 0),
                        entry(Nominal.NOMINAL_2000, 2_000),
                        entry(Nominal.NOMINAL_1000, 3_000),
                        entry(Nominal.NOMINAL_500, 0),
                        entry(Nominal.NOMINAL_200, 200),
                        entry(Nominal.NOMINAL_100, 100),
                        entry(Nominal.NOMINAL_50, 50)
                );
    }

    @Test
    void notEnoughMoney() {
        assertThatThrownBy( () -> atm.withdrawMoney(100))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessage("Not enough money: requestedAmount 100 is greater than the available amount 0");
    }
}
