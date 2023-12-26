package ru.edu.filmlibrary.library.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@Tag(name = "Роли пользователей", description = "Контроллер ролей пользователей")
public class RoleController {

    protected RoleController() {
        super();
    }
}
