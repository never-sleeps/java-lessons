package ru.neo4j.demo;

import com.google.gson.Gson;
import org.neo4j.driver.*;
import ru.neo4j.demo.model.Phone;
import ru.neo4j.demo.model.PhoneUser;
import ru.neo4j.demo.repository.Neo4jPhoneRepository;
import ru.neo4j.demo.repository.Neo4jPhoneUserRepository;
import ru.neo4j.demo.repository.PhoneRepository;
import ru.neo4j.demo.repository.PhoneUserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Результат можно смотреть в http://localhost:7474/browser/
 */
public class Demo {
    private static final int NEO4J_PORT = 7687;
     private static final String NEO4J_HOST = "localhost"; // Работа без DockerToolbox
    //private static final String NEO4J_HOST = "192.168.99.100"; // Работа через DockerToolbox
    private static final String NEO4J_URL = "bolt://" + NEO4J_HOST + ":" + NEO4J_PORT;

    public static void main(String[] args) throws Throwable {

        var samsungBada = new Phone(UUID.randomUUID().toString(), "Bada", "black", "000001");
        var sonyZ3Compact = new Phone(UUID.randomUUID().toString(), "Z3", "silver", "000002");
        var iphoneSE = new Phone(UUID.randomUUID().toString(), "se", "pink", "000003");

        var anna = new PhoneUser(UUID.randomUUID().toString(),
                "Anna", List.of(samsungBada, sonyZ3Compact));
        var john = new PhoneUser(UUID.randomUUID().toString(),
                "John", List.of(iphoneSE));

        var mapper = new Gson();
        //try (val driver = GraphDatabase.driver(NEO4J_URL, AuthTokens.basic("neo4j", "neo4j"))) {
        try (var driver = GraphDatabase.driver(NEO4J_URL)) {
            dropAllNodes(driver);

            PhoneRepository phoneRepository = new Neo4jPhoneRepository(driver, mapper);
            PhoneUserRepository phoneUserRepository = new Neo4jPhoneUserRepository(driver, mapper, phoneRepository);

            // сохраняем телефоны
            phoneRepository.insert(samsungBada);
            phoneRepository.insert(sonyZ3Compact);
            phoneRepository.insert(iphoneSE);

            // сохраняем пользователей
            phoneUserRepository.insert(anna);
            phoneUserRepository.insert(john);


            // находим телефон по id
            Optional<Phone> samsungBadaOptional = phoneRepository.findOne(samsungBada.getId());
            samsungBadaOptional.ifPresent(p -> System.out.printf("Phone from db is:\n%s\n", p));
            // Phone from db is:
            // Phone(id=6991d597-11d0-4414-9fec-224ac1ced581, model=Bada, color=black, serialNumber=000001)


            // достаём все телефоны
            List<Phone> allPhones = phoneRepository.findAll();
            System.out.println("All phones from db:\n" + allPhones.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All phones from db:
            // Phone(id=6991d597-11d0-4414-9fec-224ac1ced581, model=Bada, color=black, serialNumber=000001)
            // Phone(id=cd1d0238-045d-473b-ba2d-b348cbc3ab40, model=Z3, color=silver, serialNumber=000002)
            // Phone(id=dff1cf44-eaf1-4b1a-b765-b6ab69de25b3, model=se, color=pink, serialNumber=000003)


            // находим пользователя по id
            Optional<PhoneUser> annaOptional = phoneUserRepository.findOne(anna.getId());
            annaOptional.ifPresent(u -> System.out.printf("User from db is:\n%s\n", u));
            // User from db is:
            // PhoneUser(id=b5153f05-92b5-4784-be32-6692e60b9dca, name=Anna, phones=[Phone(id=cd1d0238-045d-473b-ba2d-b348cbc3ab40, model=Z3, color=silver, serialNumber=000002), Phone(id=6991d597-11d0-4414-9fec-224ac1ced581, model=Bada, color=black, serialNumber=000001)])


            // достаём всех пользователей
            List<PhoneUser> allPhoneUsers = phoneUserRepository.findAll();
            System.out.println("All users from db:\n" + allPhoneUsers.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All users from db:
            // PhoneUser(id=b5153f05-92b5-4784-be32-6692e60b9dca, name=Anna, phones=[Phone(id=cd1d0238-045d-473b-ba2d-b348cbc3ab40, model=Z3, color=silver, serialNumber=000002), Phone(id=6991d597-11d0-4414-9fec-224ac1ced581, model=Bada, color=black, serialNumber=000001)])
            // PhoneUser(id=07741b8a-81b9-40e0-b362-5fb1ba8341c7, name=John, phones=[Phone(id=dff1cf44-eaf1-4b1a-b765-b6ab69de25b3, model=se, color=pink, serialNumber=000003)])
        }
    }


    private static void dropAllNodes(Driver driver) {
        try (var session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
        }
    }
}
