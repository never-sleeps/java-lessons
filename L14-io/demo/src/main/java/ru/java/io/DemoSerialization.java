package ru.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DemoSerialization {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        byte[] serialized = serialize();
        deserialize(serialized);
    }

    // Serializing: Person{age=27, name='Jack', hidden='hidden secret value'}
    private static byte[] serialize() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            var person = new Person(27, "Jack", "hidden secret value");
            System.out.println("Serializing: " + person);
            objectOutputStream.writeObject(person);
            return byteArrayOutputStream.toByteArray();
        }
    }

    // deSerialized person: Person{age=27, name='Jack', hidden='null'}
    private static void deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            var person = (Person) objectInputStream.readObject();
            System.out.println("deSerialized person: " + person);
        }
    }
}
