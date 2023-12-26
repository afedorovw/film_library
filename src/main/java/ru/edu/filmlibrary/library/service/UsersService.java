package ru.edu.filmlibrary.library.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.edu.filmlibrary.library.dto.RoleDTO;
import ru.edu.filmlibrary.library.dto.UsersDTO;
import ru.edu.filmlibrary.library.mapper.UsersMapper;
import ru.edu.filmlibrary.library.model.Users;
import ru.edu.filmlibrary.library.repository.UsersRepository;

import java.time.LocalDateTime;

@Service
public class UsersService extends GenericService<Users, UsersDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository,
                        UsersMapper usersMapper,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(usersRepository, usersMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public UsersDTO create(UsersDTO newObject) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        newObject.setRole(roleDTO);
        newObject.setPassword(bCryptPasswordEncoder.encode(newObject.getPassword()));
        newObject.setCreatedBy("REGISTRATION FORM");
        newObject.setCreatedWhen(LocalDateTime.now());
        return super.create(newObject);
    }

    public UsersDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UsersRepository) repository).findUserByLogin(login));
    }

    public UsersDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UsersRepository) repository).findUserByEmail(email));
    }

    public boolean checkPassword(String password, UserDetails foundUser) {
        return bCryptPasswordEncoder.matches(password, foundUser.getPassword());
    }

}
