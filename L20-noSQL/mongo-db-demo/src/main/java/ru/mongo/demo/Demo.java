package ru.mongo.demo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import lombok.val;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import ru.mongo.demo.model.Phone;
import ru.mongo.demo.model.SmartPhone;
import ru.mongo.demo.template.MongoTemplateImpl;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Demo {
    private static final String MONGODB_URL = "mongodb://localhost:30001"; // Работа без DockerToolbox
    //private static final String MONGODB_URL = "mongodb://192.168.99.100:30001"; // Работа через DockerToolbox

    private static final String MONGO_DATABASE_NAME = "mongo-db-test";

    private static final String PHONES_COLLECTION_NAME = "phones";

    public static void main(String[] args) throws Throwable {
        var samsungBada = new Phone(null, "Bada", "black", "000001");
        var sonyZ3Compact = new SmartPhone(null, "Z3", "silver", "000002", "Android");
        var iphoneSE = new SmartPhone(null, "se", "silver", "000003", "IOS");

        // даже если до обращения бд с именем MONGODB_URL не было, в момент обращения она создастся
        ConnectionString connectionString = new ConnectionString(MONGODB_URL);
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("ru.mongo.demo.model").build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));


        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();


        try (var mongoClient = MongoClients.create(clientSettings)) {
            var database = mongoClient.getDatabase(MONGO_DATABASE_NAME);
            var collection = database.getCollection(PHONES_COLLECTION_NAME);

            database.drop();

            var mongoTemplate = new MongoTemplateImpl(PHONES_COLLECTION_NAME, database);
            mongoTemplate.insert(samsungBada);
            mongoTemplate.insert(sonyZ3Compact);
            mongoTemplate.insert(iphoneSE);

            // ищем конкретный объект по id
            var iphoneSEOptional = mongoTemplate.findOne(iphoneSE.getId(), SmartPhone.class);
            iphoneSEOptional.ifPresent(sm -> System.out.printf("Smartphone from db is:\n%s\n", sm));
            // Smartphone from db is:
            // SmartPhone(id=6170867700c1cf18498a99f8, model=se, color=silver, serialNumber=000003, operatingSystem=IOS)


            // ищем все объекты со свойством color = silver (строки эквивалентны)
            //val allSilverPhones = mongoTemplate.find(eq("color", "silver"), Phone.class);
            var allSilverPhones = mongoTemplate.find(Document.parse("{\"color\": \"silver\"}"), Phone.class);
            System.out.println("All sliver phones from db:\n" + allSilverPhones.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All sliver phones from db:
            // Phone(id=6170867700c1cf18498a99f7, model=Z3, color=silver, serialNumber=000002)
            // Phone(id=6170867700c1cf18498a99f8, model=se, color=silver, serialNumber=000003)


            // вставляем новый объект прямо через json
            collection.insertOne(Document.parse("{\"model\":\"iphone 12\", \"color\": \"blue\"}"));

            // достаём все объекты из базы
            var allPhones = mongoTemplate.findAll(Phone.class);
            System.out.println("All phones from db:\n" + allPhones.stream()
                    .map(Objects::toString).collect(Collectors.joining("\n")));
            // All phones from db:
            // Phone(id=6170867700c1cf18498a99f6, model=Bada, color=black, serialNumber=000001)
            // Phone(id=6170867700c1cf18498a99f7, model=Z3, color=silver, serialNumber=000002)
            // Phone(id=6170867700c1cf18498a99f8, model=se, color=silver, serialNumber=000003)
            // Phone(id=6170867700c1cf18498a99f9, model=iphone 12, color=blue, serialNumber=null)
        }
    }

}
