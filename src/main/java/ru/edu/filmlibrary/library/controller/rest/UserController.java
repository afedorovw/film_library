package ru.edu.filmlibrary.library.controller.rest;

import ru.edu.filmlibrary.library.config.jwt.JWTTokenUtil;
import ru.edu.filmlibrary.library.dto.LoginDTO;
import ru.edu.filmlibrary.library.dto.UsersDTO;
import ru.edu.filmlibrary.library.model.Users;
import ru.edu.filmlibrary.library.service.GenericService;
import ru.edu.filmlibrary.library.service.UsersService;
import ru.edu.filmlibrary.library.service.userdetails.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rest/users")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Пользователи",
        description = "Контроллер для работы с пользователями")
public class UserController extends GenericController<Users, UsersDTO>{

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenUtil jwtTokenUtil;
    private final UsersService userService;

    public UserController(GenericService<Users, UsersDTO> genericService,
                          CustomUserDetailsService customUserDetailsService,
                          JWTTokenUtil jwtTokenUtil,
                          UsersService userService) {
        super(genericService);
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDTO.getLogin());
        log.info("foundUser: {}", foundUser);
        if (!userService.checkPassword(loginDTO.getPassword(), foundUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка авторизации! \n Неверный пароль...");
        }
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        response.put("username", foundUser.getUsername());
        response.put("role", foundUser.getAuthorities());
        return ResponseEntity.ok().body(response);
    }
}
