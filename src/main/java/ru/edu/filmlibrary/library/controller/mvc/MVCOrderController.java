package ru.edu.filmlibrary.library.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.edu.filmlibrary.library.dto.OrderDTO;
import ru.edu.filmlibrary.library.service.FilmsService;
import ru.edu.filmlibrary.library.service.OrderService;
import ru.edu.filmlibrary.library.service.userdetails.CustomUserDetails;


@Slf4j
@Controller
@RequestMapping("/orders")
public class MVCOrderController {

    private final OrderService orderService;
    private final FilmsService filmsService;

    public MVCOrderController(OrderService orderService,
                              FilmsService filmsService) {
        this.orderService = orderService;
        this.filmsService = filmsService;
    }

    @GetMapping("/film/{filmId}")
    public String rentFilm(@PathVariable Long filmId,
                           Model model) {
        model.addAttribute("film", filmsService.getOne(filmId));
        return "orders/rentFilm";
    }

    @PostMapping("/film")
    public String rentFilm(@ModelAttribute("rentFilmInfo") OrderDTO orderDTO) {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        orderService.rentFilm(orderDTO);
        return "redirect:/orders/user-films/" + customUserDetails.getUserId();
    }

    @GetMapping("/return-film/{id}")
    public String returnFilm(@PathVariable Long id) {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.returnFilm(id);
        return "redirect:/orders/user-films/" + customUserDetails.getUserId();
    }

    @GetMapping("/user-films/{id}")
    public String userFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int pageSize,
                            @PathVariable Long id,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> orderDTOS = orderService.listUserRentFilms(id, pageRequest);
        model.addAttribute("rentFilms", orderDTOS);
        model.addAttribute("userId", id);
        return "orders/viewUserFilms";
    }
}
