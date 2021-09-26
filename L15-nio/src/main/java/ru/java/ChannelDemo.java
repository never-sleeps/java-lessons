package ru.java;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class ChannelDemo {
    public static void main(String[] args) throws IOException, URISyntaxException {
        new ChannelDemo().go();
    }

    /**
     * Чтение данных из байта в буфер, запись из буфера в StringBuilder и последующий вывод его в консоль
     * @throws IOException
     * @throws URISyntaxException
     */
    private void go() throws IOException, URISyntaxException {
        var path = Paths.get(ClassLoader.getSystemResource("share.xml").toURI());
        try (var fileChannel = FileChannel.open(path)) {
            var buffer = ByteBuffer.allocate(10); // инициализация буфера

            int bytesCount;
            var stringBuilder = new StringBuilder();
            do {
                bytesCount = fileChannel.read(buffer); // чтение из файла и запись в буфер
                buffer.flip(); // изменение режима буфера на чтение
                while (buffer.hasRemaining()) { // пока данные в буфере есть, переносим из в stringBuilder
                    stringBuilder.append((char) buffer.get());
                }
                buffer.flip(); // изменение режима буфера на запись
            } while (bytesCount > 0); // пока не закончится файл

            System.out.println(stringBuilder); // вывод результата на экран
        }
    }
}
