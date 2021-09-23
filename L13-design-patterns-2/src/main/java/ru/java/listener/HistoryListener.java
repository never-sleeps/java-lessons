package ru.java.listener;

import ru.java.model.Message;

import java.util.*;

/**
 * Listener для ведения истории
 */
public class HistoryListener implements Listener, Reader {

    private final Map<Long, Message> history;

    public HistoryListener() {
        this.history = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        history.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id))
                .map(Message::clone);
    }
}
