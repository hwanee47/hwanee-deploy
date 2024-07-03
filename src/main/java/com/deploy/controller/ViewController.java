package com.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/settings")
    public String settings() {
        return "settings/settings";
    }

    @GetMapping("/settings/credential")
    public String credential() {
        return "settings/credential";
    }

    @GetMapping("/settings/credential-edit")
    public String credentialEdit(String id, Model model) {
        model.addAttribute("id", id);
        return "settings/credential-edit";
    }

    @GetMapping("/settings/credentials")
    public String credentials() {
        return "settings/credentials";
    }

    @GetMapping("/settings/scmConfigs")
    public String scmConfigs() {
        return "settings/scmConfigs";
    }

    @GetMapping("/settings/scmConfig")
    public String scmConfig() {
        return "settings/scmConfig";
    }

    @GetMapping("/settings/scmConfig-edit")
    public String scmConfigEdit(String id, Model model) {
        model.addAttribute("id", id);
        return "settings/scmConfig-edit";
    }

}
