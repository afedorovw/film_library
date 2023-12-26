package ru.edu.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.OrderDTO;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.repository.FilmsRepository;
import ru.edu.filmlibrary.library.repository.UsersRepository;
import ru.edu.filmlibrary.library.service.FilmsService;

import java.util.List;

@Component
public class OrderMapper extends GenericMapper<Order, OrderDTO> {

    private final UsersRepository usersRepository;
    private final FilmsRepository filmsRepository;
    private final FilmsService filmsService;

    public OrderMapper(ModelMapper modelMapper,
                       UsersRepository usersRepository,
                       FilmsRepository filmsRepository,
                       FilmsService filmsService) {
        super(modelMapper, Order.class, OrderDTO.class);
        this.usersRepository = usersRepository;
        this.filmsRepository = filmsRepository;
        this.filmsService = filmsService;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {

        super.modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(mapping -> mapping.skip(OrderDTO::setUserId))
                .addMappings(mapping -> mapping.skip(OrderDTO::setFilmId))
                .addMappings(mapping -> mapping.skip(OrderDTO::setFilmsDTO))
                .setPostConverter(toDTOConverter());

        super.modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(mapping -> mapping.skip(Order::setUser))
                .addMappings(mapping -> mapping.skip(Order::setFilm))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {

        destination.setUser(usersRepository
                .findById(source.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));

        destination.setFilm(filmsRepository
                .findById(source.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден")));
    }

    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
        destination.setFilmsDTO(filmsService.getOne(source.getFilm().getId()));
    }

    @Override
    protected List<Long> getIds(Order entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}
