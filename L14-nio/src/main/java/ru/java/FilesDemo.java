package ru.java;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesDemo {
    public static void main(String[] args) throws IOException, URISyntaxException {
        new FilesDemo().go();
    }

    private void go() throws IOException, URISyntaxException {
        var path = Paths.get("L15-nio/data.xml");
        var pathExists = Files.exists(path);
        System.out.println("pathExists:" + pathExists);

        var pathNE = Paths.get("L15-nio/NE.xml");
        boolean pathNotExists = Files.exists(pathNE);
        System.out.println("\npathExists:" + pathNotExists);

        Files.createDirectory(Paths.get("L15-nio/tmp"));

        //с файлами в ресурсах надо работать как с ресурсами
        var uri = ClassLoader.getSystemResource("share.xml").toURI();

        var source = Paths.get(uri);
        var destination = Paths.get("L15-nio/tmp/share.xml");

        Files.copy(source, destination);

        var size = Files.size(path);
        System.out.println("\nfile size: " + size);

        System.out.println("\ncontentAll:");

        try (var stream = Files.lines(path)) {
            stream.forEach(System.out::println);
        }

        System.out.println("\nfiltered:");

        try (var stream = Files.lines(path)) {
            stream.filter(line -> line.contains("share"))
                    .map(String::toUpperCase)
                    .forEach(System.out::println);
        }

        var testString = "Test-Test-Data-String";
        Files.write(Paths.get("L15-nio/tmp/newFile.txt"), testString.getBytes());
    }
}
