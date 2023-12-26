package ru.edu.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.GenericModel;
import ru.edu.filmlibrary.library.repository.FilmsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DirectorsMapper extends GenericMapper<Directors, DirectorsDTO> {

    private final FilmsRepository filmsRepository;

    public DirectorsMapper(ModelMapper modelMapper,
                           FilmsRepository filmsRepository) {
        super(modelMapper, Directors.class, DirectorsDTO.class);
        this.filmsRepository = filmsRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {

        modelMapper.createTypeMap(Directors.class, DirectorsDTO.class)
                .addMappings(mapping -> mapping.skip(DirectorsDTO::setFilmsIds))
                .setPostConverter(toDTOConverter());

        modelMapper.createTypeMap(DirectorsDTO.class, Directors.class)
                .addMappings(mapping -> mapping.skip(Directors::setFilms))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(DirectorsDTO source, Directors destination) {
        if (!Objects.isNull(source.getFilmsIds())) {
            destination.setFilms(filmsRepository.findAllById(source.getFilmsIds()));
        } else {
            destination.setFilms(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Directors source, DirectorsDTO destination) {
        destination.setFilmsIds(getIds(source));
    }

    @Override
    protected List<Long> getIds(Directors source) {
        return Objects.isNull(source) || Objects.isNull(source.getFilms())
                ? Collections.emptyList()
                : source.getFilms().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}
