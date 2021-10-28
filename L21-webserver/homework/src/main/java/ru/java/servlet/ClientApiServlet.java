package ru.java.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import ru.java.hibernate.crm.model.ClientEntity;
import ru.java.hibernate.crm.service.DBServiceClient;

import java.io.BufferedReader;
import java.io.IOException;


public class ClientApiServlet extends HttpServlet {

    private final Gson gson;
    private final DBServiceClient dbServiceClient;

    public ClientApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        ClientEntity client = gson.fromJson(reader, ClientEntity.class);
        dbServiceClient.saveClient(client);
        resp.setStatus(HttpStatus.CREATED_201);
    }
}
