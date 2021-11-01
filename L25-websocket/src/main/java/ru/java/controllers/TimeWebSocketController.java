package ru.java.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.java.config.AppConfig.DATE_TIME_FORMAT;

@Controller
public class TimeWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(TimeWebSocketController .class);
    private final SimpMessagingTemplate template;

    public TimeWebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /*
        Раз в секунду отправляем сообщение всем, кто подписался на '/topic/currentTime' (в нашем случае, браузеру)
     */
    @Scheduled(fixedDelay = 1000)
    public void broadcastCurrentTime() {
        logger.info("sent timeRequest");
        template.convertAndSend(
                "/topic/currentTime",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        );
    }
}
