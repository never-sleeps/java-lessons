package ru.java.listener;

import ru.java.model.Message;

import java.util.Optional;

public interface Reader {

    Optional<Message> findMessageById(long id);

}
