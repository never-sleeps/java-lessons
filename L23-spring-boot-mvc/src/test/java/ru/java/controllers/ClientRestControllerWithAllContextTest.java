package ru.java.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.java.domain.Client;
import ru.java.services.ClientService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    В рамках этого теста будет поднят весь контекст (благодаря @SpringBootTest).
    Будет запущено сканирование пакетов наверх, пока не будет встречен SpringBootConfiguration или SpringBootApplication,
    после этого сканируются пакеты внутрь и поднимаются все найденный конфиги и бины, кроме тех, которые отмечены в тесте аннотацией @MockBean.
    Для них в контексте будет создан Mock. Т.е. таким образом мы может поднять контекст, но что-то в нём замокать.
 */
@DisplayName("WithContextTest: REST-контроллер клиентов ")
@SpringBootTest
@AutoConfigureMockMvc
class ClientRestControllerWithAllContextTest {

    @Autowired // внедряется благодаря @AutoConfigureMockMvc
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @DisplayName("должен возвращать корректного клиента по его id")
    @Test
    void shouldReturnExpectedClientById() throws Exception {
        Client expectedClient = new Client(1, "Крис Гир");

        given(clientService.findById(1L)).willReturn(expectedClient);

        Gson gson = new GsonBuilder().create();
        mvc.perform(get("/api/client/{id}", 1L).accept("application/json; charset=utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(expectedClient)));
    }
}