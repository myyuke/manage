package com.example.demo.repository;


import com.example.demo.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoginRepository extends JpaRepository<UserLogin,Integer> {

    public UserLogin findByUsername(String username);
    @Query(value = "select * from user_login where person_id=?", nativeQuery = true)
    UserLogin myFindPId(Integer id);

}
