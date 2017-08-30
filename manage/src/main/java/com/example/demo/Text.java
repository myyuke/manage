package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Text {
    @GetMapping(value = "/hello")
    public String say(){
        return "hello2";
    }
}
