package com.example.demo.errorcatch;
//权限错误
public class MyException extends Exception{
        public MyException(String message){
            super(message);
        }
}