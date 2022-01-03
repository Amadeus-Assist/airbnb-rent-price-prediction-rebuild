package com.columbia.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String index() {
        System.out.println("get");
        return "Greetings from Spring Boot!";
    }

}
