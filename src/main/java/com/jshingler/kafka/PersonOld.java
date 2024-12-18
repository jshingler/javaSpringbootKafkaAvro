package com.jshingler.kafka;

import java.util.Objects;

public class PersonOld {
    private String firstName;
    private String lastName;
    private String address;

    // Constructors
    public PersonOld() {}

    public PersonOld(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Equals and HashCode for testing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonOld personOld = (PersonOld) o;
        return Objects.equals(firstName, personOld.firstName) &&
                Objects.equals(lastName, personOld.lastName) &&
                Objects.equals(address, personOld.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address);
    }
}
