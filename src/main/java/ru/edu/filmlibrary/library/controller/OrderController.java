package ru.edu.filmlibrary.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.edu.filmlibrary.library.dto.OrderDTO;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.service.OrderService;

@RestController
@RequestMapping("/orders")
@Tag(name = "Запрос фильма", description = "Контроллер покупки фильмов")
public class OrderController extends GenericController<Order, OrderDTO> {

    public OrderController(OrderService orderService) {
        super(orderService);
    }

    @Operation(description = "Покупка фильма", method = "rentFilm")
    @RequestMapping(value = "/rentFilm",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> rentFilm(@RequestParam(value = "userId") Long userId,
                                             @RequestParam(value = "filmId") Long filmId,
                                             @RequestParam(value = "purchase") Boolean purchase,
                                             @RequestParam(value = "rentPeriod") Integer rentPeriod) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(((OrderService) service).rentFilm(userId, filmId, purchase, rentPeriod));
    }
}
