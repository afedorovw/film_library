package ru.edu.filmlibrary.library.mapper;

import ru.edu.filmlibrary.library.dto.GenericDTO;
import ru.edu.filmlibrary.library.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {
    E toEntity(D dto);

    D toDTO(E entity);

    List<E> toEntities(List<D> dtos);

    List<D> toDTOs(List<E> entities);

}
