package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Logrecord {

    @Id
    @GeneratedValue
    private Integer id;

    private String data;

    private String behavior;

    private Integer personId;

    private String personName;


    public Logrecord() {
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "Logrecord{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", personId=" + personId +
                ", personName='" + personName + '\'' +
                '}';
    }
}
