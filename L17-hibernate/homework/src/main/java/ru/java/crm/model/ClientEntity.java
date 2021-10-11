package ru.java.crm.model;

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

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhoneEntity> phones = Collections.emptyList();

    public ClientEntity() { }

    public ClientEntity(String name) {
        this.name = name;
    }

    public ClientEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public ClientEntity(String name, AddressEntity address, List<PhoneEntity> phones) {
        this.name = name;
        this.address = address;
        phones.forEach(phone -> phone.setClient(this));
        this.phones = phones;
    }

    @Override
    public ClientEntity clone() {
        List<PhoneEntity> phonesCopy = new ArrayList<>();
        this.phones.forEach(phone -> phonesCopy.add(phone.clone()));
        ClientEntity clientCopy = new ClientEntity(this.id, this.name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientEntity client = (ClientEntity) o;

        if (!id.equals(client.id)) return false;
        if (!name.equals(client.name)) return false;
        if (address != null ? !address.equals(client.address) : client.address != null) return false;
        return phones != null ? phones.equals(client.phones) : client.phones == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
