package ru.redis.demo;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import ru.redis.demo.model.Phone;
import ru.redis.demo.model.SmartPhone;
import ru.redis.demo.template.RedisTemplateImpl;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


public class Demo {
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_HOST = "localhost"; // Работа без DockerToolbox
    //private static final String REDIS_HOST = "192.168.99.100"; // Работа через DockerToolbox

    public static void main(String[] args) throws Throwable {
        var samsungBada = new Phone(UUID.randomUUID(), "Bada", "black", "000001");
        var sonyZ3Compact = new SmartPhone(UUID.randomUUID(), "Z3", "silver", "000002", "Android");
        var iphoneSE = new SmartPhone(UUID.randomUUID(), "se", "pink", "000003", "IOS");

        var mapper = new Gson();
        var jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        var redisTemplate = new RedisTemplateImpl(jedis, mapper);

        jedis.flushAll(); // Delete all the keys of all the existing databases, not just the currently selected one.

        redisTemplate.insert(samsungBada.getId().toString(), samsungBada);
        redisTemplate.insert(sonyZ3Compact.getId().toString(), sonyZ3Compact);
        redisTemplate.insert(iphoneSE.getId().toString(), iphoneSE);


        var samsungBadaOptional = redisTemplate.findOne(samsungBada.getId().toString(), Phone.class);
        samsungBadaOptional.ifPresent(sm -> System.out.printf("Smartphone from db is:\n%s\n", sm));
        // Smartphone from db is:
        // Phone(id=1b77c5c5-5819-4289-b8a0-8d73852b896d, model=Bada, color=black, serialNumber=000001)

        var sonyZ3CompactOptional = redisTemplate.findOne(sonyZ3Compact.getId().toString(), SmartPhone.class);
        sonyZ3CompactOptional.ifPresent(sm -> System.out.printf("Phone from db is:\n%s\n", sm));
        // Phone from db is:
        // SmartPhone(id=73aaf34f-49fb-4538-b4b7-be462f7b2bac, model=Z3, color=silver, serialNumber=000002, operatingSystem=Android)


        var iphoneSEOptional = redisTemplate.findOne(iphoneSE.getId().toString(), SmartPhone.class);
        iphoneSEOptional.ifPresent(sm -> System.out.printf("Smartphone from db is:\n%s\n", sm));
        // Smartphone from db is:
        // SmartPhone(id=5e0e5f6d-27c9-444a-a348-a74b83b7c006, model=se, color=pink, serialNumber=000003, operatingSystem=IOS)


        var allPhones = redisTemplate.findAll(Phone.class);
        System.out.println("All phones from db:\n" + allPhones.stream()
                .map(Objects::toString).collect(Collectors.joining("\n")));
        // All phones from db:
        // Phone(id=1b77c5c5-5819-4289-b8a0-8d73852b896d, model=Bada, color=black, serialNumber=000001)

        var allSmartPhones = redisTemplate.findAll(SmartPhone.class);
        System.out.println("All SmartPhone from db:\n" + allSmartPhones.stream()
                .map(Objects::toString).collect(Collectors.joining("\n")));
        // All SmartPhone from db:
        // SmartPhone(id=5e0e5f6d-27c9-444a-a348-a74b83b7c006, model=se, color=pink, serialNumber=000003, operatingSystem=IOS)
        // SmartPhone(id=73aaf34f-49fb-4538-b4b7-be462f7b2bac, model=Z3, color=silver, serialNumber=000002, operatingSystem=Android)
    }
}
