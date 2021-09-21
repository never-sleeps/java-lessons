package ru.java.processor;

import ru.java.model.Message;

/**
 * процессор, который меняет местами значения field11 и field12
 */
public class ProcessorSwapFields11And12 implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field12(message.getField11())
                .field11(message.getField12())
                .build();
    }
}
