package ru.java;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CopyFilesDemo {
    public static void main(String[] args) throws IOException {
        copyFiles("textFile.txt", "textFile.bak");
    }

    /**
     * Копирование файла.
     *
     * Если операционная система умеет копировать файлы напрямую из одного FileChannel в другой, операционная память
     * применяться при таком виде копирования не будет. Процедура копирования будет настолько быстрой, насколько она
     * может быть такой в этой операционной системе. В противном случае будет использовано обычное копирование.
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    private static void copyFiles(String srcFile, String destFile) throws IOException {
        try (var channelSrc = FileChannel.open(Path.of(srcFile), StandardOpenOption.READ);
             var channelDest = FileChannel.open(Path.of(destFile), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            channelSrc.transferTo(0, channelSrc.size(), channelDest);
        }
    }
}
