package ru.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.java.dao.InMemoryUserDao;
import ru.java.dao.UserDao;
import ru.java.server.UsersWebServer;
import ru.java.server.UsersWebServerSimple;
import ru.java.services.TemplateProcessor;
import ru.java.services.TemplateProcessorImpl;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithoutSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new UsersWebServerSimple(
                WEB_SERVER_PORT,
                userDao,
                gson,
                templateProcessor
        );

        usersWebServer.start();
        usersWebServer.join();
    }
}
