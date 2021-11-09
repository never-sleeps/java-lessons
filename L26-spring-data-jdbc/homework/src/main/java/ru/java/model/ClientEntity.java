package ru.java.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Set;

@Table("client")
public class ClientEntity {

    @Id
    private Long id;

    @Nonnull
    private String login;

    @Nonnull
    private String password;

    @MappedCollection(idColumn = "client_id")
    private AddressEntity address;

    @MappedCollection(idColumn = "client_id")
    private Set<PhoneEntity> phones;

    @PersistenceConstructor
    private ClientEntity(Long id, String login, String password, AddressEntity address, Set<PhoneEntity> phones) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phones = phones;
    }

    public ClientEntity(String login, String password, AddressEntity address, Set<PhoneEntity> phones) {
        this(null, login, password, address, phones);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public Set<PhoneEntity> getPhones() {
        return phones;
    }

    public void setPhones(Set<PhoneEntity> phones) {
        this.phones = phones;
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
}
