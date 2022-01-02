package ru.java.front;

import ru.java.dto.ClientDto;

public interface FrontendService {
    void getAll();

    void createClient(ClientDto clientDto);

    void deleteClient(ClientDto id);
}
