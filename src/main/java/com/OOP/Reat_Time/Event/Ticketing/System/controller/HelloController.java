package com.OOP.Reat_Time.Event.Ticketing.System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("hello")
    public String greet(){
        return "welcome real time event ticketing system";
    }
}
