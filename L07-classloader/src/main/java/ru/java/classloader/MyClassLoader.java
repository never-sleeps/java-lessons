package ru.java.classloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyClassLoader extends ClassLoader {
    Class<?> defineClass(String classDirectory, String className) throws IOException {
        var file = new File(getFileName(classDirectory, className));
        byte[] bytecode = Files.readAllBytes(file.toPath());
        return super.defineClass(className, bytecode, 0, bytecode.length);
    }

    String getFileName(String classDirectory, String className) {
        return classDirectory +
                File.separator +
                className.substring(className.lastIndexOf('.') + 1) +
                ".class";
    }
}
