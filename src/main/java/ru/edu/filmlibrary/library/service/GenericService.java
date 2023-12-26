package ru.edu.filmlibrary.library.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.GenericDTO;
import ru.edu.filmlibrary.library.mapper.GenericMapper;
import ru.edu.filmlibrary.library.model.GenericModel;
import ru.edu.filmlibrary.library.repository.GenericRepository;

import java.util.List;

@Service
public abstract class GenericService<E extends GenericModel, D extends GenericDTO> {
    final GenericRepository<E> repository;
    final GenericMapper<E, D> mapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericService(GenericRepository<E> repository, GenericMapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<D> listAll() {
        return mapper.toDTOs(repository.findAll());
    }

    public D getOne(final Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Данные по id:" + id + " не найдены")));
    }

    public D create(D newObject) {
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    public D update(D updateObject) {
        return mapper
                .toDTO(repository.save(mapper.toEntity(updateObject)));
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }
}
