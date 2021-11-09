package ru.java.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import ru.java.dto.AddressDto;


@Table("address")
public class AddressEntity  {
    @Id
    private Long id;

    private String city;

    private String street;

    private String house;

    private Long clientId;

    @PersistenceConstructor
    private AddressEntity(Long id, String city, String street, String house, Long clientId) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.clientId = clientId;
    }

    public AddressEntity(AddressDto addressDto) {
        this.city = addressDto.getCity();
        this.street = addressDto.getStreet();
        this.house = addressDto.getHouse();
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}