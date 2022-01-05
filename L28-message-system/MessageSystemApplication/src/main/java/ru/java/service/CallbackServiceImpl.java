package ru.java.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.app.messagesystem.client.ResultDataType;

import java.util.List;

@Service
public class CallbackServiceImpl<T extends ResultDataType> implements CallbackService<T> {

    private final SimpMessagingTemplate template;

    public CallbackServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void callback(T data) {
        template.convertAndSend("/", data);
    }

    @Override
    public void callbackForBatch(List<T> data) {
        template.convertAndSend("/clients/page", data);
    }

}
