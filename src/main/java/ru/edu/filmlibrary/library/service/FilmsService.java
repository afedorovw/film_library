package ru.edu.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.constants.Errors;
import ru.edu.filmlibrary.library.dto.FilmSearchDTO;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.mapper.FilmsMapper;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;
import ru.edu.filmlibrary.library.repository.FilmsRepository;
import ru.edu.filmlibrary.library.repository.OrderRepository;
import ru.edu.filmlibrary.library.utils.FileHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FilmsService
        extends GenericService<Films, FilmsDTO>
        implements Errors {

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

    public FilmsDTO create(final FilmsDTO newFilm,
                           MultipartFile file) {
        String fileName = FileHelper.createFile(file);
        newFilm.setOnlineCopyPath(fileName);
        newFilm.setCreatedWhen(LocalDateTime.now());
        newFilm.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return mapper.toDTO(repository.save(mapper.toEntity(newFilm)));
    }

    public void setStars(FilmsDTO filmsDTO) {
    }

    public Page<FilmsDTO> getAllFilms(Pageable pageable) {
        Page<Films> filmPaginated = repository.findAll(pageable);
        List<FilmsDTO> result = mapper.toDTOs(filmPaginated.getContent());
        return new PageImpl<>(result, pageable, filmPaginated.getTotalElements());
    }

    public Page<FilmsDTO> searchFilm(FilmSearchDTO filmSearchDTO,
                                     Pageable pageRequest) {

        String genre = filmSearchDTO.getGenre() != null
                ? String.valueOf(filmSearchDTO.getGenre().ordinal())
                : null;

        Page<Films> filmsPaginated = ((FilmsRepository) repository).searchFilms(
                filmSearchDTO.getFilmTitle(),
                genre,
                filmSearchDTO.getDirectorsFio(),
                pageRequest
        );

        List<FilmsDTO> result = mapper.toDTOs(filmsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, filmsPaginated.getTotalElements());
    }

    public Page<FilmsDTO> searchFilm(final String title,
                                     Pageable pageable) {
        Page<Films> films = ((FilmsRepository) repository).findAllByTitleContainsIgnoreCase(title, pageable);
        List<FilmsDTO> result = mapper.toDTOs(films.getContent());
        return new PageImpl<>(result, pageable, films.getTotalElements());
    }

    public FilmsDTO addDirector(final Long filmId,
                                final Long directorId) {
        FilmsDTO film = getOne(filmId);
        Directors directors = directorsRepository.findById(directorId).orElseThrow(() ->
                new NotFoundException("Режиссер не найден"));
        film.getDirectorsId().add(directors.getId());
        update(film);
        return film;
    }

    @Override
    public void deleteSoft(final Long id) throws MyDeleteException {
        Films films = repository.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        boolean filmCanBeDeleted = ((FilmsRepository) repository).isFilmCanBeDeleted(id);
        if (filmCanBeDeleted) {
            markAsDeleted(films);
            repository.save(films);
        } else {
            throw new MyDeleteException(Film.FILM_DELETE_ERROR);
        }
    }

    public List<FilmsDTO> showAllRentedFilmsByUserId(final Long userId) {
        Iterable<Order> ordersIterable = orderRepository.findAll();
        List<FilmsDTO> films = new ArrayList<>();
        for (Order order : ordersIterable) {
            if (Objects.equals(userId, order.getUser().getId()) && !order.getPurchase()) {
                FilmsDTO filmDTO = getOne(order.getFilm().getId());
                films.add(filmDTO);
            }
        }
        return films;
    }
}
