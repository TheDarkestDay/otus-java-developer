package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    public Address() {
    }

    public Address(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }

        Address addressLikeObj = (Address) obj;

        return addressLikeObj.getStreet().equals(getStreet()) && addressLikeObj.id == id;
    }
}
