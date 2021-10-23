package ru.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.java.dao.InMemoryUserDao;
import ru.java.dao.UserDao;
import ru.java.helpers.FileSystemHelper;
import ru.java.server.UsersWebServer;
import ru.java.server.UsersWebServerWithBasicSecurity;
import ru.java.services.InMemoryLoginServiceImpl;
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
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);

        // логин/пароль/роль будут браться из realm.properties
//        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        // логин/пароль/роль будут браться из userDao
        LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(
                WEB_SERVER_PORT,
                loginService,
                userDao,
                gson,
                templateProcessor
        );

        usersWebServer.start();
        usersWebServer.join();
    }
}
