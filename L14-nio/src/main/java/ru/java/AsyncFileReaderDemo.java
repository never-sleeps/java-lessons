package ru.java;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/*
Пример применения в GridGain:
https://github.com/gridgain/gridgain/blob/d6222c6d892eabcbcfc60fd75fc2d38a7dd06bb6/modules/core/src/main/java/org/apache/ignite/internal/processors/cache/persistence/file/AsyncFileIO.java
 */

public class AsyncFileReaderDemo implements AutoCloseable {
    private final ByteBuffer buffer = ByteBuffer.allocate(2);
    private final AsynchronousFileChannel fileChannel;
    private final StringBuilder fileContent = new StringBuilder();

    private final CompletionHandler<Integer, ByteBuffer> completionHandler = new CompletionHandler<>() {
        private int lastPosition = 0;

        /**
         * Когда готова к чтению какая-то порция байт, вызывается этот метод.
         * Вызывается для каждой порции байт заново. С помощью него за определенное количество итераций вычитывается весь файл.
         * @param readBytes - количество байт
         * @param attachment - буфер с самими данными
         */
        @Override
        public void completed(Integer readBytes, ByteBuffer attachment) {
            System.out.println(Thread.currentThread().getName() + ". readBytes:" + readBytes);
            byte[] destArray;
            if (readBytes > 0) { // проверка наличия прочитанных байт
                destArray = new byte[readBytes]; // создаём массив необходимой для полученных байт длины
                attachment.flip(); // переворачиваем буфер для режима чтения
                attachment.get(destArray, 0, destArray.length); // перекладываем в наш массив байты

                lastPosition += readBytes; // сдвигаем позицию
                read(fileChannel, lastPosition, buffer); // запускаем процедуру чтения уже с новой позиции
                fileContent.append(new String(destArray));
            } else { // окончание чтения (дошли до конца файла)
                System.out.println(Thread.currentThread().getName() + " fileData:\n" + fileContent);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            System.err.println("error:" + exc.getMessage());
        }

        /**
         * Чтение из файла.
         * Выполняется в отдельном потоке от потока основного приложения.
         * Созданием этого потока и управлением им занимается AsynchronousFileChannel.
         *
         * @param fileChannel - канал, по которому должно производиться чтение
         * @param position - позиция, с которой должно производиться чтение
         * @param buffer - буфер, в который будет производиться чтение
         */
        private void read(AsynchronousFileChannel fileChannel, int position, ByteBuffer buffer) {
            buffer.clear();
            fileChannel.read(buffer, position, buffer, completionHandler);
            // completionHandler - хендлер, метод из которого будет вызван, при завершении чтения порции байт
        }
    };


    public static void main(String[] args) throws Exception {
        try (var asyncRead = new AsyncFileReaderDemo()) {
            asyncRead.read();
        }
    }

    /**
     * Открываем файл на чтение
     * @throws IOException
     */
    public AsyncFileReaderDemo() throws IOException {
        fileChannel = AsynchronousFileChannel.open(Path.of("textFile.txt"), StandardOpenOption.READ);
    }

    private void read() throws InterruptedException {
        // вызов чтения файла. поток main не будет дожидаться окончания этой команды и будет продолжать работу дальше
        fileChannel.read(
                buffer, // dst
                0, // position
                buffer, // attachment
                completionHandler // обработчик, который будет вызываться при прочтении определенной порции файла
        );

        System.out.println(Thread.currentThread().getName() + " Hello");

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
    }
}
