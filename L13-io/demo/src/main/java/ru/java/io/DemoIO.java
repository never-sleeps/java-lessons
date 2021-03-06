package ru.java.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class DemoIO {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("current dir: " + System.getProperty("user.dir"));  // ../java-lessons/L14-io/demo
         copyFile("textFile.txt", "archiveWithCopy");
//         writeObject("person.txt", new Person(25, "Ben", "hidden"));
//         readObject("person.txt");
//         writeTextFile("textFile.txt");
//         readTextFile("textFile.txt");

    }

    /**
     * Создаст архив [archiveName] с файлом  - копией файла [fileForCopyName]
     * @param fileForCopyName - имя файла, который будет скопирован
     * @param archiveName - имя архива, который будет создан
     * @throws IOException
     */
    private static void copyFile(String fileForCopyName, String archiveName) throws IOException {
        try (var bufferedInputStream = new BufferedInputStream(new FileInputStream(fileForCopyName));
             var zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream( archiveName + ".zip")))) {

            var zipEntry = new ZipEntry(fileForCopyName);
            zipOut.putNextEntry(zipEntry);
            var buffer = new byte[2];
            int size;
            while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) > 0) {
                zipOut.write(buffer, 0, size);
            }
            zipOut.closeEntry();
        }
    }

    /**
     * Создаст файл [newFileName] и запишет туда данные объекта [object]
     * Пример содержания файла: �� sr ru.java.io.Person        I ageL namet Ljava/lang/String;xp   t Ben
     * @param newFileName
     * @throws IOException
     */
    private static void writeObject(String newFileName, Object object) throws IOException {
        try (var objectOutputStream = new ObjectOutputStream(new FileOutputStream(newFileName))) {

            System.out.println("serializing:" + object); // serializing:Person{age=25, name='Ben', hidden='hidden'}
            objectOutputStream.writeObject(object);
        }
    }

    /**
     * Десериализация объекта из файла [fileForDeserializationName].
     * Тип объекта определяется автоматически, но можно использовать и явное приведение: (Person) objectInputStream.readObject();
     * @param fileForDeserializationName - имя файла, откуда будет десериализован объект
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void readObject(String fileForDeserializationName) throws IOException, ClassNotFoundException {
        try (var objectInputStream = new ObjectInputStream(new FileInputStream(fileForDeserializationName))) {

            var object = objectInputStream.readObject();
            System.out.println("read object:" + object); // read object:Person{age=25, name='Ben', hidden='null'}
        }
    }

    /**
     * Запись текста в файл [newFileName]
     * @param newFileName - имя файла, который будет создан
     * @throws IOException
     */
    private static void writeTextFile(String newFileName) throws IOException {
        var line1 = "Hello Java, str1";
        var line2 = "Hello Java, str2";
        try (var bufferedWriter = new BufferedWriter(new FileWriter(newFileName))) {
            bufferedWriter.write(line1);
            bufferedWriter.newLine();
            bufferedWriter.write(line2);
        }
    }

    /**
     * Чтение текста из файла [fileForReadingName]
     * @param fileForReadingName - - имя файла, из которого будет производиться чтение
     * @throws IOException
     */
    private static void readTextFile(String fileForReadingName) throws IOException {
        try (var bufferedReader = new BufferedReader(new FileReader(fileForReadingName))) {
            System.out.println("text from the file:");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
