package ru.java.services;

import ru.java.model.Equation;
import ru.java.model.Player;
import ru.java.services.EquationPreparerService;
import ru.java.services.GameService;
import ru.java.services.IOService;
import ru.java.services.PlayerService;

import java.util.List;

public class GameServiceImpl implements GameService {

    private static final String MSG_HEADER = "Проверка знаний таблицы умножения";
    private static final String MSG_INPUT_BASE = "Введите цифру от 1 до 10";
    private static final String MSG_RIGHT_ANSWER = "Молодец\n";
    private static final String MSG_WRONG_ANSWER = "Ошибка\n";

    private final IOService ioService;
    private final EquationPreparerService equationPreparerService;
    private final PlayerService playerService;

    public GameServiceImpl(
            IOService ioService,
            EquationPreparerService equationPreparerService,
            PlayerService playerService
    ) {
        this.ioService = ioService;
        this.equationPreparerService = equationPreparerService;
        this.playerService = playerService;
    }

    @Override
    public void startGame() {
        ioService.out(MSG_HEADER);
        ioService.out("---------------------------------------");
        Player player = playerService.getPlayer();

        int base = ioService.readInt(MSG_INPUT_BASE);
        List<Equation> equations = equationPreparerService.prepareEquationsFor(base);
        equations.forEach(e -> {
            boolean isRight = ioService.readInt(e.toString()) == e.getResult();
            player.incrementRightAnswers(isRight);
            ioService.out(isRight? MSG_RIGHT_ANSWER : MSG_WRONG_ANSWER);
        });
        ioService.out(player.getResultString());
    }
}
