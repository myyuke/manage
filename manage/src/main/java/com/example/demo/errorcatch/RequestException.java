package com.example.demo.errorcatch;

public class RequestException extends Exception{
    public RequestException(String message){
        super(message);
    }
}
