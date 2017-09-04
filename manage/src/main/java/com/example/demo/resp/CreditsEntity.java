package com.example.demo.resp;

import com.example.demo.model.Position;

import java.util.HashSet;
import java.util.Set;

public class CreditsEntity {

    private Integer personId;

    private String personName;

    private Integer personAge;

    private Set<Position> position;

    private String token;

    public CreditsEntity() {
        position = new HashSet<>();
    }


    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getPersonAge() {
        return personAge;
    }

    public void setPersonAge(Integer personAge) {
        this.personAge = personAge;
    }

    public Set<Position> getPosition() {
        return position;
    }

    public void setPosition(Set<Position> position) {
        this.position = position;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "CreditsEntity{" +
                "personId=" + personId +
                ", personName='" + personName + '\'' +
                ", personAge=" + personAge +
                ", position=" + position +
                '}';
    }
}
