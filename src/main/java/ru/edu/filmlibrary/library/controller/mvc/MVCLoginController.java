package ru.edu.filmlibrary.library.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class MVCLoginController {
    @GetMapping
    public String login() {
        return "users/login";
    }
}
