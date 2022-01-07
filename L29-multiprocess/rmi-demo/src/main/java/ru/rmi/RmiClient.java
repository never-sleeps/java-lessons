package ru.rmi;

import java.rmi.Naming;

/**
 * Технология RMI (Java Remote Method Invocation) позволяет java-приложению, запущенному на одной виртуальной машине,
 * вызвать методы объекта, работающего на другой виртуальной машине JVM (Java Virtual Machine).
 */
public class RmiClient {
    public static void main(String[] args) throws Exception {
        String address = String.format("//localhost:%d/EchoServer", RmiServer.REGISTRY_PORT);
        EchoInterface echoInterface = (EchoInterface) Naming.lookup(address); // в итоге получаем прокси, который внутри себя и будет обращаться к удалённому методу
        var dataFromServer = echoInterface.echo("hello");
        System.out.printf("response from the server: %s%n", dataFromServer);
    }
}
