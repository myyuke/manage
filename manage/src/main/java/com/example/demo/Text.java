package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Text {
    @GetMapping(value = "/hello123")
    public String say(){
        return "hello2";
    }
}
