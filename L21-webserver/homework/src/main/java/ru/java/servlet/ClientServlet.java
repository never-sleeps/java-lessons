package ru.java.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.java.hibernate.crm.model.ClientEntity;
import ru.java.hibernate.crm.service.DBServiceClient;
import ru.java.service.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "client.html";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<ClientEntity> clients = dbServiceClient.findAll();
        paramsMap.put("clients", clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
