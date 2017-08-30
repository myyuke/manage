package com.example.demo.model;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Position {

    @Id
    @GeneratedValue
    private Integer id;

    private String positionName;


    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch=FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Person> person;
    public Position() {
        this.person=new HashSet<>();

    }


    public Set<Person> getPerson() {
        return person;
    }

    public void setPerson(Set<Person> person) {
        this.person = person;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", positionName='" + positionName + '\'' +
                ", person=" + person +
                '}';
    }
}
