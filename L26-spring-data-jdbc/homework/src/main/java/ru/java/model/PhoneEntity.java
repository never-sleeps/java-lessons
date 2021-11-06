package ru.java.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import ru.java.dto.PhoneDto;


@Table("phone")
public class PhoneEntity {
    @Id
    private Long id;

    private String number;

    private Long clientId;

    @PersistenceConstructor
    private PhoneEntity(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public PhoneEntity(PhoneDto phoneDto) {
        this.number = phoneDto.getNumber();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
