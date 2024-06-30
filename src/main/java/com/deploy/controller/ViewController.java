package com.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/view")
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }


    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }
}
