package ru.edu.filmlibrary.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.edu.filmlibrary.library.constants.MailConstants;
import ru.edu.filmlibrary.library.dto.RoleDTO;
import ru.edu.filmlibrary.library.dto.UsersDTO;
import ru.edu.filmlibrary.library.mapper.GenericMapper;
import ru.edu.filmlibrary.library.model.Users;
import ru.edu.filmlibrary.library.repository.GenericRepository;
import ru.edu.filmlibrary.library.repository.UsersRepository;
import ru.edu.filmlibrary.library.utils.MailUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UsersService extends GenericService<Users, UsersDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;

    public UsersService(GenericRepository<Users> repository,
                        GenericMapper<Users, UsersDTO> mapper,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        JavaMailSender javaMailSender) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UsersDTO create(UsersDTO newObject) {
        if (Objects.isNull(newObject.getRole())) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(1L);
            newObject.setRole(roleDTO);
        }
        newObject.setPassword(bCryptPasswordEncoder.encode(newObject.getPassword()));
        newObject.setCreatedBy("REGISTRATION FORM");
        newObject.setCreatedWhen(LocalDateTime.now());
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));

    }

    public void createModerator(UsersDTO newObject) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        newObject.setRole(roleDTO);
        newObject.setCreatedBy("MODERATOR CREATION FORM");
        create(newObject);
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

    public void sendChangePasswordEmail(final UsersDTO userDTO) {
        UUID uuid = UUID.randomUUID();
        log.info("Generated Token: {} ", uuid);

        userDTO.setChangePasswordToken(uuid.toString());
        update(userDTO);

        SimpleMailMessage mailMessage = MailUtils.createMailMessage(
                userDTO.getEmail(),
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid
        );

        javaMailSender.send(mailMessage);

    }

    public void changePassword(String uuid, String password) {
        UsersDTO userDTO = mapper.toDTO(((UsersRepository) repository).findUserByChangePasswordToken(uuid));
        userDTO.setChangePasswordToken(null);
        userDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(userDTO);
    }

    public List<String> getUserEmailsWithDelayedRentDate() {
        return ((UsersRepository) repository).getDelayedEmails();
    }

    public Page<UsersDTO> findUsers(UsersDTO userDTO,
                                    Pageable pageable) {
        Page<Users> users = ((UsersRepository) repository).searchUsers(userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getLogin(),
                pageable);
        List<UsersDTO> result = mapper.toDTOs(users.getContent());
        return new PageImpl<>(result, pageable, users.getTotalElements());
    }

}
