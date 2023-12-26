package ru.edu.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.AddFilmDTO;
import ru.edu.filmlibrary.library.constants.Errors;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.mapper.DirectorsMapper;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;

import java.util.List;

@Service
public class DirectorsService extends GenericService<Directors, DirectorsDTO> {

    public DirectorsService(DirectorsRepository directorsRepository,
                            DirectorsMapper directorsMapper) {
        super(directorsRepository, directorsMapper);
    }

    public DirectorsDTO addFilm(final AddFilmDTO addFilmDTO) {
        DirectorsDTO directorsDTO = getOne(addFilmDTO.getDirectorId());
        directorsDTO.getFilmsIds().add(addFilmDTO.getFilmId());
        update(directorsDTO);
        return directorsDTO;
    }

    public Page<DirectorsDTO> searchDirectors(final String fio,
                                              Pageable pageable) {
        Page<Directors> directors = ((DirectorsRepository) repository)
                .searchAllByDirectorsFIOContainingIgnoreCase(fio, pageable);
        List<DirectorsDTO> result = mapper.toDTOs(directors.getContent());
        return new PageImpl<>(result, pageable, directors.getTotalElements());
    }

    @Override
    public void deleteSoft(Long objectId) throws MyDeleteException {
        Directors directors = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Режиссера с заданным id=" + objectId + " не существует."));
        boolean directorCanBeDeleted = ((DirectorsRepository) repository).checkDirectorForDeletion(objectId);
        if (directorCanBeDeleted) {
            markAsDeleted(directors);
            List<Films> films = directors.getFilms();
            if (films != null && films.size() > 0) {
                films.forEach(this::markAsDeleted);
            }
            ((DirectorsRepository) repository).save(directors);
        } else {
            throw new MyDeleteException(Errors.Director.DIRECTOR_DELETE_ERROR);
        }
    }

    public void restore(Long objectId) {
        Directors directors = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Режиссера с заданным id=" + objectId + " не существует."));
        unMarkAsDeleted(directors);
        List<Films> films = directors.getFilms();
        if (films != null && films.size() > 0) {
            films.forEach(this::unMarkAsDeleted);
        }
        repository.save(directors);
    }
}
