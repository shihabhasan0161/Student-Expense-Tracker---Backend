package com.studentexpensetracker.studentexpensetracker.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class HomeController {
    @GetMapping
    public String testStatus() {
        return "Application is running!";
    }
}
