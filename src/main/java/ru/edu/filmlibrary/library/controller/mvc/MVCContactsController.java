package ru.edu.filmlibrary.library.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/contacts")
public class MVCContactsController {

    @GetMapping
    public String contact() {
        return "contacts/viewContacts";
    }
}
