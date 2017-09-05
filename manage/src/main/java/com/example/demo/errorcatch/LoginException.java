package com.example.demo.errorcatch;

public class LoginException extends Exception{
    public LoginException(String message){
        super(message);
    }
}
