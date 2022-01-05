package ru.java.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.java.dto.ClientDto;
import ru.java.front.FrontendService;

@Controller
public class ClientController {
    private final FrontendService frontendService;
    
    public ClientController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @MessageMapping("/all")
    @SendTo("/clients/page")
    public void getClients() { frontendService.getAll(); }

    @MessageMapping("/create")
    @SendTo("/clients/page")
    public void createClient(ClientDto client) {
        frontendService.createClient(client);
    }
}
