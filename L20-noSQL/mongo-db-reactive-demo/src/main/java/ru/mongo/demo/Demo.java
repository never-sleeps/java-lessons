package ru.mongo.demo;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import ru.mongo.demo.subscribers.ObservableSubscriber;
import ru.mongo.demo.helpers.ReactiveMongoHelper;

import java.util.List;

public class Demo {

    public static final String MONGODB_URL = "mongodb://localhost:30001"; // Работа без DockerToolbox
    //public static final String MONGODB_URL = "mongodb://192.168.99.100:30001"; // Работа через DockerToolbox
    private static final String DB_NAME = "mongo-db-test";
    private static final String PRODUCTS_COLLECTION = "products";

    public static void main(String[] args) throws Throwable {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(PRODUCTS_COLLECTION);

            ReactiveMongoHelper.dropDatabase(database);

            doInsertAndFindDemo(collection);
        }
    }

    private static void doInsertAndFindDemo(MongoCollection<Document> collection) throws Throwable {
        var doc = new Document("key", System.currentTimeMillis())
                .append("item", "apple")
                .append("qty", 112);

        System.out.println("Insert one document");
        var subscriber = new ObservableSubscriber<InsertOneResult>();
        // вставляем документ и подписываемся на событие вставки
        collection.insertOne(doc).subscribe(subscriber);

        // имитируем что-то очень важное
        subscriber.await();


        System.out.println("Find all apples");
        var subscriberPrinter = new ObservableSubscriber<Document>();
        // поиск всех объектов с item = apple и подписка на поиск
        collection.find(Document.parse("{\"item\":\"apple\"}")).subscribe(subscriberPrinter);
        // имитируем что-то очень важное
        subscriberPrinter.await();

        // забираем у subscriberPrinter (подписка на поиск) результат работы
        List<Document> results = subscriberPrinter.getResult();
        System.out.println(String.format("result.size: %d", results.size()));

        /*
            Insert one document
            onNext, result: AcknowledgedInsertOneResult{insertedId=BsonObjectId{value=61708d1be41d4d040a2f0b1b}}
            Find all apples
            onNext, result: Document{{_id=61708d1be41d4d040a2f0b1b, key=1634766107132, item=apple, qty=112}}
            result.size: 1
         */
    }
}
