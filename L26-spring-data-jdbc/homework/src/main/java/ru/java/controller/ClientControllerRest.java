package ru.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.java.model.ClientEntity;
import ru.java.service.DBServiceClient;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
public class ClientControllerRest {

    private final DBServiceClient clientService;

    public ClientControllerRest(DBServiceClient clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientEntity> findById(@PathVariable long id) {
        var client = clientService.getClient(id)
                .orElseThrow(() -> new NoSuchElementException("Not found client by id: " + id));
        return ResponseEntity.ok(client);
    }
}
