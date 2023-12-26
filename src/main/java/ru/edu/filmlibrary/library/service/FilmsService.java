package ru.edu.filmlibrary.library.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.mapper.FilmsMapper;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;
import ru.edu.filmlibrary.library.repository.FilmsRepository;
import ru.edu.filmlibrary.library.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FilmsService extends GenericService<Films, FilmsDTO> {

    private final DirectorsRepository directorsRepository;
    private final OrderRepository orderRepository;

    public FilmsService(FilmsRepository filmsRepository,
                        FilmsMapper filmsMapper,
                        DirectorsRepository directorsRepository,
                        OrderRepository orderRepository) {
        super(filmsRepository, filmsMapper);
        this.directorsRepository = directorsRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public FilmsDTO create(FilmsDTO newObject) {
        return super.create(newObject);
    }

    public FilmsDTO addDirector(final Long filmId, final Long directorId) {

        FilmsDTO film = getOne(filmId);

        Directors director = directorsRepository
                .findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиесер не найден"));

        film.getDirectorsId().add(director.getId());
        update(film);

        return film;
    }

    public List<FilmsDTO> getAllFilmsByUserId(final Long userId) {

        Iterable<Order> ordersIterable = orderRepository.findAll();

        List<FilmsDTO> films = new ArrayList<>();
        for (Order order : ordersIterable) {
            if (Objects.equals(userId, order.getUser().getId())) {
                FilmsDTO filmDTO = getOne(order.getFilm().getId());
                films.add(filmDTO);
            }
        }
        return films;
    }
}
