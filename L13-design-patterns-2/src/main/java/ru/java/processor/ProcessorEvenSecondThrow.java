package ru.java.processor;

import ru.java.exception.EvenSecondException;
import ru.java.model.Message;

import java.time.LocalDateTime;

/**
 * Процессор, который выбрасывает исключение в четную секунду
 */
public class ProcessorEvenSecondThrow implements Processor {

    private final LocalDateTime dateTime;

    public ProcessorEvenSecondThrow(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isEvenSecond() {
        return dateTime.getSecond() % 2 == 0;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond()) {
            throw new EvenSecondException();
        }
        return message;
    }

}
