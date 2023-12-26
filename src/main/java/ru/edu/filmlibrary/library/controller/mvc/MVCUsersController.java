package ru.edu.filmlibrary.library.controller.mvc;

import jakarta.websocket.server.PathParam;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.edu.filmlibrary.library.constants.Errors;
import ru.edu.filmlibrary.library.constants.UserRolesConstants;
import ru.edu.filmlibrary.library.dto.UsersDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.service.UsersService;
import org.springframework.validation.BindingResult;
import ru.edu.filmlibrary.library.service.userdetails.CustomUserDetails;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Controller
@RequestMapping("/users")
public class MVCUsersController implements UserRolesConstants {

    private final UsersService usersService;

    public MVCUsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UsersDTO());
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UsersDTO userDTO,
                               BindingResult bindingResult) {
        if (userDTO.getLogin().equalsIgnoreCase(ADMIN) ||
                usersService.getUserByLogin(userDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login",
                    "Такой логин уже существует");
            return "users/registration";
        }
        if (usersService.getUserByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email",
                    "Такой e-mail уже существует");
            return "users/registration";
        }
        usersService.create(userDTO);
        return "redirect:users/login";
    }

    @GetMapping("/remember-password")
    public String rememberPassword() {
        return "users/rememberPassword";
    }

    @PostMapping("/remember-password")
    public String rememberPassword(@ModelAttribute("changePasswordForm") UsersDTO userDTO) {
        userDTO = usersService.getUserByEmail(userDTO.getEmail());
        if (Objects.isNull(userDTO)) {
            return "error/work";
        } else {
            usersService.sendChangePasswordEmail(userDTO);
            return "redirect:users/login";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 Model model) {
        model.addAttribute("uuid", uuid);
        return "users/changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@PathParam(value = "uuid") String uuid,
                                 @ModelAttribute("changePasswordForm") UsersDTO userDTO) {
        usersService.changePassword(uuid, userDTO.getPassword());
        return "redirect:users/login";
    }

    @GetMapping("/change-password/user")
    public String changePassword(Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsersDTO userDTO = usersService.getOne(Long.valueOf(customUserDetails.getUserId()));
        UUID uuid = UUID.randomUUID();
        userDTO.setChangePasswordToken(uuid.toString());
        usersService.update(userDTO);
        model.addAttribute("uuid", uuid);
        return "users/changePassword";
    }

    @GetMapping
    public String getAll(Model model) {
        List<UsersDTO> users = usersService.listAll();
        model.addAttribute("users", users);
        return "users/viewAllUsers";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable(value = "id") Long userId, Model model) {
        UsersDTO user = usersService.getOne(userId);
        model.addAttribute("profileForm", user);
        return "users/profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String edition(@PathVariable Integer id,
                          Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId())) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
        }
        model.addAttribute("userForm", usersService.getOne(Long.valueOf(id)));
        return "users/editProfile";
    }

    @PostMapping("/profile/edit")
    public String edition(@ModelAttribute("editForm") UsersDTO userDTO,
                          Principal principal) {
        log.info(userDTO.toString());
        UsersDTO user = usersService.getUserByLogin(principal.getName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        usersService.update(user);
        return "redirect:/users/profile/" + user.getId();
    }

    @GetMapping("/add-moderator")
    public String addLibrarianPage(Model model) {
        model.addAttribute("userForm", new UsersDTO());
        return "users/addModerator";
    }

    @PostMapping("/add-moderator")
    public String addModerator(@ModelAttribute("userForm") UsersDTO userDTO,
                               BindingResult bindingResult) {
        if (userDTO.getLogin().equalsIgnoreCase(ADMIN) || usersService.getUserByLogin(userDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Такой логин уже существует");
            return "/users/registration";
        }
        if (usersService.getUserByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Такой email уже существует");
            return "/users/registration";
        }
        usersService.createModerator(userDTO);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        usersService.deleteSoft(id);
        return "redirect:/users";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        usersService.restore(id);
        return "redirect:/users";
    }

    @PostMapping("/search")
    public String searchUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @ModelAttribute("userSearchForm") UsersDTO userDTO,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "first_name"));
        model.addAttribute("users", usersService.findUsers(userDTO, pageRequest));
        return "users/viewAllUsers";
    }

    @ExceptionHandler({AuthException.class})
    public RedirectView handleError(HttpServletRequest request,
                                    Exception ex,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос " + request.getRequestURL() + " вызвал ошибку " + ex.getMessage());
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redirectAttributes.addFlashAttribute("exception", ex.getMessage());
        return new RedirectView("/users/profile/" + customUserDetails.getUserId(), true);
    }

}
