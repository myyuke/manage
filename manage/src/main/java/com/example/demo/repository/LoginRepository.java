package com.example.demo.repository;

import com.example.demo.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<UserLogin,Integer> {

    public UserLogin findByUsername(String username);

}
