package ru.edu.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.model.GenericModel;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;
import ru.edu.filmlibrary.library.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FilmsMapper extends GenericMapper<Films, FilmsDTO> {

    private final DirectorsRepository directorsRepository;
    private final OrderRepository orderRepository;

    public FilmsMapper(ModelMapper modelMapper,
                       DirectorsRepository directorsRepository,
                       OrderRepository orderRepository) {
        super(modelMapper, Films.class, FilmsDTO.class);
        this.directorsRepository = directorsRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {

        modelMapper.createTypeMap(Films.class, FilmsDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(FilmsDTO::setDirectorsId);
                    mapping.skip(FilmsDTO::setFilmOrderId);
                }).setPostConverter(toDTOConverter());

        modelMapper.createTypeMap(FilmsDTO.class, Films.class)
                .addMappings(mapping -> mapping.skip(Films::setDirectors))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(FilmsDTO source, Films destination) {
        if (!Objects.isNull(source.getDirectorsId())) {
            destination.setDirectors(directorsRepository.findAllById(source.getDirectorsId()));
            destination.setOrders(orderRepository.findAllById(source.getFilmOrderId()));
        }
    }

    @Override
    protected void mapSpecificFields(Films source, FilmsDTO destination) {
        destination.setDirectorsId(getIds(source));
        destination.setFilmOrderId(getOrderIds(source));
    }

    @Override
    protected List<Long> getIds(Films source) {
        return Objects.isNull(source) || Objects.isNull(source.getDirectors())
                ? Collections.emptyList()
                : source.getDirectors().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }

    protected List<Long> getOrderIds(Films source) {
        return Objects.isNull(source) || Objects.isNull(source.getOrders())
                ? Collections.emptyList()
                : source.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}
