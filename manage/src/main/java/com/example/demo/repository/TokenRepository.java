package com.example.demo.repository;

import com.example.demo.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Integer> {
//    @Query(value = "select * from token where person_id=?", nativeQuery = true)
//    Token myFindPId(Integer id);
//
//    public Token findByToken(String token);

}