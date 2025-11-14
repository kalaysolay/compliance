package com.example.compliance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер авторизации.
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
