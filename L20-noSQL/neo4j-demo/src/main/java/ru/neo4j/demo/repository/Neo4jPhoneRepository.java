package ru.neo4j.demo.repository;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import ru.neo4j.demo.model.Phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Neo4jPhoneRepository implements PhoneRepository {

    private final Driver driver;
    private final Gson mapper;

    @Override
    public void insert(Phone phone) {
        try (var session = driver.session()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", phone.getId());
            params.put("model", phone.getModel());
            params.put("color", phone.getColor());
            params.put("serialNumber", phone.getSerialNumber());
            session.run("CREATE (n:Phone {id: $id, model: $model, color: $color, serialNumber: $serialNumber})", params);
            // n - нода, которой поставить в соответствие метку Phone, а внутри она будет содержать указанный набор полей
        }

    }

    @Override
    public Optional<Phone> findOne(String id) {
        try (var session = driver.session()) {
            Result result = session.run("MATCH (n:Phone {id: $id}) " +
                    "RETURN \"{id: \" + n.id + \", " +
                    "model: \" + n.model + \", " +
                    "color: \" + n.color + \", " +
                    "serialNumber: \" + n.serialNumber + \"}\" as res", Map.of("id", id));
            // найти совпадения с нодой, у которой метка Phone и id = указанному
            return Optional.ofNullable(result.single().get("res"))
                    .map(r ->  mapper.fromJson(r.asString(), Phone.class));
        }
    }

    @Override
    public List<Phone> findAll() {
        try (var session = driver.session()) {
            Result result = session.run("MATCH (n:Phone) " +
                    "RETURN \"{id: \" + n.id + \", " +
                    "model: \" + n.model + \", " +
                    "color: \" + n.color + \", " +
                    "serialNumber: \" + n.serialNumber + \"}\" as res");
            // найти совпадения с нодой, у которой метка Phone
            return result.list().stream()
                    .map(r -> mapper.fromJson(r.get("res").asString(), Phone.class))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Phone> findAllByUserId(String userId) {
        try (var session = driver.session()) {
            Result result = session.run("MATCH (p:Phone)<--(u:PhoneUser {id: $userId}) " +
                    "RETURN \"{id: \" + p.id + \", " +
                    "model: \" + p.model + \", " +
                    "color: \" + p.color + \", " +
                    "serialNumber: \" + p.serialNumber + \"}\" as res", Map.of("userId", userId));
            // найти совпадения с нодой, у которой метка Phone, который в этом направлении (<--)
            // связаны с нодой, у которой метка PhoneUser и id = указанному
            return result.list().stream()
                    .map(r -> mapper.fromJson(r.get("res").asString(), Phone.class))
                    .collect(Collectors.toList());
        }
    }
}
