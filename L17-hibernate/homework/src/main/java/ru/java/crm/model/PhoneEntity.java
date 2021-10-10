package ru.java.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneEntity implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_sequence_generator")
    @SequenceGenerator(name = "phone_sequence_generator", sequenceName = "phone_sequence", allocationSize = 1)
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    public PhoneEntity() { }

    public PhoneEntity(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public PhoneEntity(String number) {
        this.number = number;
    }

    @Override
    public PhoneEntity clone() {
        return new PhoneEntity(this.id, this.number);
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

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneEntity phone = (PhoneEntity) o;

        if (id != phone.id) return false;
        if (!number.equals(phone.number)) return false;
        return client.equals(phone.client);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + number.hashCode();
        result = 31 * result + client.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
