package ru.java.model;

/**
 * Сущность: Игрок
 */
public class Player {
    private final String name;
    private int total;
    private int rightAnswers;

    private static final String RESULT_PATTERN = "Уважаемый: %s. Всего было примеров: %d, отвечено верно: %d";

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void incrementRightAnswers(boolean mustIncremented) {
        total++;
        rightAnswers = mustIncremented? ++rightAnswers: rightAnswers;
    }

    public String getResultString() {
        return String.format(RESULT_PATTERN, getName(),total, rightAnswers);
    }
}
