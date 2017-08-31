package com.example.demo.model;

import javax.persistence.*;

@Entity
public class Person_Position {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "personId")
    private Person person;

    private Integer positionId;

    public Person_Position() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}
