package ru.java.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.app.messagesystem.RequestHandler;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.Message;

import java.util.Optional;

public class ResponseHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        log.info("new message:{}", msg);
        try {
            var callback = msg.getCallback();
            if (callback != null) {
                callback.accept(msg.getData());
            } else {
                log.error("callback for Id:{} not found", msg.getId());
            }
        } catch (Exception ex) {
            log.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
