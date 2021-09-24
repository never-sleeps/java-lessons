package ru.java.protobuf;

import com.google.protobuf.util.JsonFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        var fileName = "protoTest.bin";

        var helloMessage = HelloWorld.HelloMessage.newBuilder()
                .setMessage("Hello, World, from Protobuf")
                .addArrayIntData(10)
                .addArrayIntData(20)
                .addArrayIntData(30)
                .build();

        var messageJson = JsonFormat.printer().print(helloMessage);
        System.out.println("messageJson:" + messageJson);
        // messageJson:{
        //  "message": "Hello, World, from Protobuf",
        //  "arrayIntData": [10, 20, 30]
        //}

        var builder = HelloWorld.HelloMessage.newBuilder();
        JsonFormat.parser().merge(messageJson, builder);
        var messageFromJson = builder.build();
        System.out.println("messageFromJson:" + messageFromJson.getMessage() + ", array:" + messageFromJson.getArrayIntDataList());
        // messageFromJson:Hello, World, from Protobuf, array:[10, 20, 30]

        try (var fileOutputStream = new FileOutputStream(fileName)) {
            helloMessage.writeTo(fileOutputStream);
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            var messageIn = HelloWorld.HelloMessage.parseFrom(fileInputStream);
            System.out.println("messageIn:" + messageIn.getMessage() + ",  array:" + messageIn.getArrayIntDataList());
            // messageIn:Hello, World, from Protobuf,  array:[10, 20, 30]
        }
    }
}
