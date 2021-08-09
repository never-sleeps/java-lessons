package ru.java.bytecode;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/*
javap - команда для получения информации о любом классе или интерфейсе

javac HelloWorld.java

// печать дополнительной информации, такую как размер стека, количество локальных и аргументы для методов.
javap -v HelloWorld.class

javap -private HelloWorld.class

// « -c » или « -verbose » для байтового кода и байтового кода вместе с внутренней информацией, соответственно
javap -c -verbose HelloWorld.class
*/

public class CreatingClassByAsmApplication {

    public static void main(String[] args) throws Exception {
        var className = "HelloWorld";
        var methodName = "printHelloWorld";

        //Генератор классов
        var classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(
                Opcodes.V11,
                Opcodes.ACC_PUBLIC,
                className,
                null,
                "java/lang/Object",
                null
        );

        //Конструктор класса
        MethodVisitor constructor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        constructor.visitCode();

        //Вызываем конструктор предка (Object)
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
        );
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();

        //Создаем метод printHelloWorld
        MethodVisitor methodVisitor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                methodName,
                "()V",
                null,
                null
        );
        methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
        );
        methodVisitor.visitLdcInsn("Hello, World!");
        methodVisitor.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false
        );
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        //Загружаем полученный класс
        Class<?> helloWorldClass = new MyClassLoader().defineClass(className, classWriter.toByteArray());

        //Выполняем метод main
        var method = helloWorldClass.getMethod(methodName);
        method.invoke(null);

        // Сохраняем class в файл .class (необязательно, в данном случае для наглядности)
        printToFile(className, classWriter);
    }

    private static void printToFile(String className, ClassWriter classWriter) {
        try (OutputStream fileOutputStream = new FileOutputStream(
                "L08-bytecode-by-asm" + File.separator + "classes" + File.separator +
                        className + ".class"
        )) {
            fileOutputStream.write(classWriter.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
