package com.example.demo.control;


import com.example.demo.model.Token;
import com.example.demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TokenControl {

    @Autowired
    private TokenRepository tokenRepository;

    //登入数据储存
    public void loginToken(Integer personId){
//        UUID uuid=UUID.randomUUID();
//        Token token1 = new Token();
//        if(tokenRepository.findOne(personId)!=null){
//            token1 = tokenRepository.findOne(personId);
//            if(token1.getToken().equals(uuid.toString())){
//            }else {
//                token1.setToken(uuid.toString());
//                tokenRepository.save(token1);
//            }
//        }
//        token1.setId(personId);
//        token1.setToken(uuid.toString());
//        tokenRepository.save(token1);
    }
}
