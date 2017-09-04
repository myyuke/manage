package com.example.demo.model;

import javax.persistence.*;

@Entity
public class UserLogin {
    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String password;

    @OneToOne(cascade= CascadeType.MERGE)
    @JoinColumn(name="personId")
    private Person person;

    public UserLogin() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", person=" + person +
                '}';
    }
}
