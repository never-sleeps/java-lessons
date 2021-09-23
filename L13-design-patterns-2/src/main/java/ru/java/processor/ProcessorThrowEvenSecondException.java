package ru.java.processor;

import ru.java.exception.EvenSecondException;
import ru.java.model.Message;

/**
 * Процессор, который выбрасывает исключение в четную секунду
 */
public class ProcessorThrowEvenSecondException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowEvenSecondException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    private boolean isEvenSecond() {
        return dateTimeProvider.getDataTime().getSecond() % 2 == 0;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond()) {
            throw new EvenSecondException();
        }
        return message;
    }

}
