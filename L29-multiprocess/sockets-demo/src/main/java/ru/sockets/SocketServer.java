package ru.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Сколько одновременных подключений может обработать такой сервер? Ответ: 1
 *
 * Чтобы одновременных подключений могло быть больше, следует каждую обработку (handleClientConnection) запускать в отдельном потоке.
 * Для экономии ресурсов вместо создания нового потока на каждый раз можно использовать ThreadPool
 */
public class SocketServer {
    private static final int PORT = 8090;

    public static void main(String[] args) {
        new SocketServer().go();
    }

    /**
     * from client: ##0 - I Believe
     * from client: ##1 - I Believe
     * from client: ##2 - I Believe
     * from client: stop
     */
    private void go() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClientConnection(clientSocket);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleClientConnection(Socket clientSocket) {
        try (
                PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); //куда будем отдавать клиенту данные
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) //откуда будем брать данные
        ) {
            String input = null;
            while (!"stop".equals(input)) {
                input = in.readLine();
                if (input != null) {
                    System.out.println(String.format("from client: %s", input));
                    outputStream.println(String.format("%s I Can Fly!", input));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }
}
