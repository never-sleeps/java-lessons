package ru.java.dto;

import ru.java.model.ClientEntity;
import ru.app.messagesystem.client.ResultDataType;

public class ClientDto implements ResultDataType {
    private String id;
    private String login;
    private String password;
    private String address;
    private String phone;

    public ClientDto() { }

    public ClientDto(ClientEntity clientEntity) {
        this.id = clientEntity.getId().toString();
        this.login = clientEntity.getLogin();
        this.password = "*".repeat(clientEntity.getPassword().length());
        this.address = clientEntity.getAddress().getCity();
        this.phone = clientEntity.getPhones().getNumber();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
