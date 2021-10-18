package ru.java.hibernate.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequence_generator")
    @SequenceGenerator(name = "address_sequence_generator", sequenceName = "address_sequence", allocationSize = 1)
    private long id;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house")
    private String house;

    public AddressEntity() { }

    public AddressEntity(long id, String regionCode, String city, String street, String house) {
        this.id = id;
        this.regionCode = regionCode;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public AddressEntity(String regionCode, String city, String street, String house) {
        this.regionCode = regionCode;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    @Override
    public AddressEntity clone() {
        return new AddressEntity(this.id, this.regionCode, this.city, this.street, this.house);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressEntity address = (AddressEntity) o;

        if (id != address.id) return false;
        if (!regionCode.equals(address.regionCode)) return false;
        if (!city.equals(address.city)) return false;
        if (!street.equals(address.street)) return false;
        return house.equals(address.house);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + regionCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + house.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", regionCode='" + regionCode + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                '}';
    }
}
