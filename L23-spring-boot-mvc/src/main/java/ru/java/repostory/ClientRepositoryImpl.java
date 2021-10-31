package ru.java.repostory;

import org.springframework.stereotype.Repository;
import ru.java.domain.Client;
import ru.java.generators.ClientIdGenerator;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final List<Client> clients = new ArrayList<>();
    private final ClientIdGenerator idGenerator;

    public ClientRepositoryImpl(ClientIdGenerator idGenerator) {
        this.idGenerator = idGenerator;

        clients.add(new Client(idGenerator.generateId(), "John Williams"));
        clients.add(new Client(idGenerator.generateId(), "Amanda Scott"));
        clients.add(new Client(idGenerator.generateId(), "Nick Brown"));
        clients.add(new Client(idGenerator.generateId(), "Charlotta Richardson"));
        clients.add(new Client(idGenerator.generateId(), "Taylor Davies"));
    }

    @Override
    public List<Client> findAll() {
        return clients;
    }

    @Override
    public Client save(Client client) {
        client.setId(idGenerator.generateId());
        clients.add(client);
        return client;
    }

    @Override
    public Client findById(long id) {
        return clients.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Client findByName(String name) {
        return clients.stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
