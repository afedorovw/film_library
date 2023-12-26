package ru.edu.filmlibrary.library.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.OrderDTO;
import ru.edu.filmlibrary.library.mapper.OrderMapper;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.model.Users;
import ru.edu.filmlibrary.library.repository.FilmsRepository;
import ru.edu.filmlibrary.library.repository.OrderRepository;
import ru.edu.filmlibrary.library.repository.UsersRepository;

import java.time.LocalDateTime;

@Service
public class OrderService extends GenericService<Order, OrderDTO> {

    private final FilmsRepository filmsRepository;

    private final UsersRepository usersRepository;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,
                        FilmsRepository filmsRepository, UsersRepository usersRepository) {
        super(orderRepository, orderMapper);
        this.filmsRepository = filmsRepository;
        this.usersRepository = usersRepository;
    }

    public OrderDTO rentFilm(Long userId,
                             Long filmId,
                             Boolean purchase,
                             Integer rentPeriod) {
        OrderDTO orderDTO = new OrderDTO();

        Users user = usersRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользовательне найден"));

        Films film = filmsRepository
                .findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        orderDTO.setFilmId(film.getId());
        orderDTO.setUserId(user.getId());
        orderDTO.setPurchase(purchase);
        orderDTO.setRentDate(LocalDateTime.now().plusMonths(rentPeriod));

        return mapper.toDTO(repository.save(mapper.toEntity(orderDTO)));
    }
}
