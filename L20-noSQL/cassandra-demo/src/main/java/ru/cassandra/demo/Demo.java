package ru.cassandra.demo;

import ru.cassandra.demo.model.Phone;
import ru.cassandra.demo.model.SmartPhone;
import ru.cassandra.demo.schema.CassandraPhonesSchemaInitializer;
import ru.cassandra.demo.db.CassandraConnection;
import ru.cassandra.demo.db.PhoneRepositoryImpl;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Demo {

    private static final int CASSANDRA_PORT = 9042;
    private static final String CASSANDRA_HOST = "localhost"; // Работа без DockerToolbox
    //private static final String CASSANDRA_HOST = "192.168.99.100"; // Работа через DockerToolbox

    public static void main(String[] args) throws Throwable {

        var samsungBada = new Phone(UUID.randomUUID(), "Bada", "black", "000001");
        var sonyZ3Compact = new SmartPhone(UUID.randomUUID(), "Z3", "silver", "000002", "Android");
        var iphoneSE = new SmartPhone(UUID.randomUUID(), "se", "pink", "000003", "IOS");

        try (CassandraConnection connector = new CassandraConnection(CASSANDRA_HOST,
                CASSANDRA_PORT)) {
            var initializer = new CassandraPhonesSchemaInitializer(connector);
            var repository = new PhoneRepositoryImpl(connector);

            initializer.dropSchemaIfExists();
            initializer.initSchema();

            repository.insert(samsungBada, Phone.class);
            repository.insert(sonyZ3Compact, SmartPhone.class);
            repository.insert(iphoneSE, SmartPhone.class);


            var samsungBadaOptional = repository.findOne(samsungBada.getId(), Phone.class);
            samsungBadaOptional.ifPresent(sm -> System.out.printf("Phone from db is:\n%s\n", sm));
            // Phone from db is:
            // Phone(id=e5487761-9167-49c5-bf78-ec180df9b387, model=Bada, color=black, serialNumber=000001)

            var sonyZ3CompactOptional = repository.findOne(sonyZ3Compact.getId(), SmartPhone.class);
            sonyZ3CompactOptional.ifPresent(sm -> System.out.printf("SmartPhone from db is:\n%s\n", sm));
            // SmartPhone from db is:
            // SmartPhone(id=2768d137-83e5-4692-888d-6a341f312264, model=Z3, color=silver, serialNumber=000002, operatingSystem=Android)

            var iphoneSEOptional = repository.findOne(iphoneSE.getId(), SmartPhone.class);
            iphoneSEOptional.ifPresent(sm -> System.out.printf("SmartPhone from db is:\n%s\n", sm));
            // SmartPhone from db is:
            // SmartPhone(id=d433c1e3-c19c-4403-a05e-08e347973008, model=se, color=pink, serialNumber=000003, operatingSystem=IOS)

            var allPhones = repository.findAll(Phone.class);
            System.out.println("All phones from db:\n" + allPhones.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All phones from db:
            // Phone(id=d433c1e3-c19c-4403-a05e-08e347973008, model=se, color=pink, serialNumber=000003)
            // Phone(id=2768d137-83e5-4692-888d-6a341f312264, model=Z3, color=silver, serialNumber=000002)
            // Phone(id=e5487761-9167-49c5-bf78-ec180df9b387, model=Bada, color=black, serialNumber=000001)

            var allSmartPhones = repository.findAll(SmartPhone.class);
            System.out.println("All SmartPhone from db:\n" + allSmartPhones.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All SmartPhone from db:
            // SmartPhone(id=d433c1e3-c19c-4403-a05e-08e347973008, model=se, color=pink, serialNumber=000003, operatingSystem=IOS)
            // SmartPhone(id=2768d137-83e5-4692-888d-6a341f312264, model=Z3, color=silver, serialNumber=000002, operatingSystem=Android)
            // SmartPhone(id=e5487761-9167-49c5-bf78-ec180df9b387, model=Bada, color=black, serialNumber=000001, operatingSystem=null)
        }
    }
}
