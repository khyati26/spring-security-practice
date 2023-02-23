package com.example.springsecuritydemo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/loginPage")
    public String viewLoginPage() {
        return "loginPage";
    }

}
