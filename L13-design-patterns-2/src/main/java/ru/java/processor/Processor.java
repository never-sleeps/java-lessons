package ru.java.processor;

import ru.java.model.Message;

public interface Processor {

    Message process(Message message);

}
