package ru.java.hibernate.crm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "client")
public class ClientEntity implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence_generator")
    @SequenceGenerator(name = "client_sequence_generator", sequenceName = "client_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhoneEntity> phones = Collections.emptyList();

    public ClientEntity() { }

    public ClientEntity(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ClientEntity(Long id, String login, String password) {
        this.login = login;
        this.password = password;
        this.id = id;
    }

    public ClientEntity(String login, String password, AddressEntity address, List<PhoneEntity> phones) {
        this.login = login;
        this.password = password;
        this.address = address;
        phones.forEach(phone -> phone.setClient(this));
        this.phones = phones;
    }

    @Override
    public ClientEntity clone() {
        List<PhoneEntity> phonesCopy = new ArrayList<>();
        this.phones.forEach(phone -> phonesCopy.add(phone.clone()));
        ClientEntity clientCopy = new ClientEntity(this.id, this.login, this.password);
        if (this.address != null) {
            clientCopy.setAddress(this.address.clone());
        }
        phonesCopy.forEach(phone -> phone.setClient(clientCopy));
        clientCopy.setPhones(phonesCopy);
        return clientCopy;
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

    public List<PhoneEntity> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneEntity> phones) {
        phones.forEach(phone -> phone.setClient(this));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity that = (ClientEntity) o;

        if (!id.equals(that.id)) return false;
        if (!login.equals(that.login)) return false;
        if (!password.equals(that.password)) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return phones != null ? phones.equals(that.phones) : that.phones == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        return result;
    }
}
