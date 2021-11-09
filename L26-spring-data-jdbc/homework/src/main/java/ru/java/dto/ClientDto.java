package ru.java.dto;

public class ClientDto {
    private String login;
    private String password;
    private AddressDto address;
    private PhoneDto phone;

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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public PhoneDto getPhone() {
        return phone;
    }

    public void setPhone(PhoneDto phone) {
        this.phone = phone;
    }
}
