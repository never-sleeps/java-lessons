package ru.java.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;


@Table("address")
public class AddressEntity  {
    @Id
    private Long id;

    private String city;

    private Long clientId;

    @PersistenceConstructor
    private AddressEntity(Long id, String city, Long clientId) {
        this.id = id;
        this.city = city;
        this.clientId = clientId;
    }

    public AddressEntity(String city) {
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}