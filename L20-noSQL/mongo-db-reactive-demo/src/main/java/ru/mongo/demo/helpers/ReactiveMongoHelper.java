package ru.mongo.demo.helpers;

import com.mongodb.reactivestreams.client.MongoDatabase;
import ru.mongo.demo.subscribers.ObservableSubscriber;

public final class ReactiveMongoHelper {

    private ReactiveMongoHelper() {
    }

    public static void dropDatabase(MongoDatabase database) throws Throwable {
        var subscriber = new ObservableSubscriber<Void>(false);
        // без подписки на событие удаления базы данных удаления базы не произойдет
        // (по аналогии с работой терминальных операций в streams)
        database.drop().subscribe(subscriber);
        subscriber.await();
    }
}
