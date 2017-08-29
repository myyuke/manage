package com.example.demo.model;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Position {

    @Id
    @GeneratedValue
    private Integer id;

    private String position_name;

    @ManyToMany(fetch=FetchType.EAGER,mappedBy = "position")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Person> person;

    public Position() {

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

    public String getPositionname() {
        return position_name;
    }

    public void setPosition_name(String positionname) {
        this.position_name = positionname;
    }
}
