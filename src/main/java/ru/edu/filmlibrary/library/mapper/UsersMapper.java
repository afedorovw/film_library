package ru.edu.filmlibrary.library.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.edu.filmlibrary.library.dto.UsersDTO;
import ru.edu.filmlibrary.library.model.Users;
import ru.edu.filmlibrary.library.model.GenericModel;
import ru.edu.filmlibrary.library.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UsersMapper extends GenericMapper<Users, UsersDTO> {

    private final OrderRepository orderRepository;

    public UsersMapper(ModelMapper modelMapper,
                       OrderRepository orderRepository) {
        super(modelMapper, Users.class, UsersDTO.class);
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {

        modelMapper.createTypeMap(Users.class, UsersDTO.class)
                .addMappings(mapping -> mapping.skip(UsersDTO::setUserBooksRent))
                .setPostConverter(toDTOConverter());

        modelMapper.createTypeMap(UsersDTO.class, Users.class)
                .addMappings(mapping -> mapping.skip(Users::setOrders))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(UsersDTO source, Users destination) {
        if (!Objects.isNull(source.getUserBooksRent())) {
            destination.setOrders(orderRepository.findAllById(source.getUserBooksRent()));
        } else {
            destination.setOrders(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Users source, UsersDTO destination) {
        destination.setUserBooksRent(getIds(source));
    }

    @Override
    protected List<Long> getIds(Users source) {
        return Objects.isNull(source) || Objects.isNull(source.getOrders())
                ? Collections.emptyList()
                : source.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}
