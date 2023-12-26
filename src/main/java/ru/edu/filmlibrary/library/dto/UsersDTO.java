package ru.edu.filmlibrary.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UsersDTO extends GenericDTO {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String email;
    private Integer wallet;
    private RoleDTO role;
    private String changePasswordToken;
    private List<Long> userFilmRent;
    private boolean isDeleted;
}
