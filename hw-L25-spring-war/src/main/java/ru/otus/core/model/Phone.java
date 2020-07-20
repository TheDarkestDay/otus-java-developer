package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Phone() {
    }

    public Phone(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User owner) {
        user = owner;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Phone)) {
            return false;
        }

        Phone phoneLikeObj = (Phone) obj;
        return phoneLikeObj.id == id && phoneLikeObj.number.equals(number);
    }
}
