package ru.edu.filmlibrary.library.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.mapper.DirectorsMapper;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;
import ru.edu.filmlibrary.library.repository.FilmsRepository;

@Service
public class DirectorsService extends GenericService<Directors, DirectorsDTO> {

    private final FilmsRepository filmsRepository;

    public DirectorsService(DirectorsRepository directorsRepository,
                            DirectorsMapper directorsMapper,
                            FilmsRepository filmsRepository) {
        super(directorsRepository, directorsMapper);
        this.filmsRepository = filmsRepository;
    }

    public DirectorsDTO addFilm(final Long filmId, final Long directorId) {

        Films film = filmsRepository
                .findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        DirectorsDTO director = getOne(directorId);
        director.getFilmsIds().add(film.getId());
        update(director);

        return director;
    }
}
