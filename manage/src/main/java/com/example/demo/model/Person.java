package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.sql.Update;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Integer age;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "person")


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
