package ru.edu.filmlibrary.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.edu.filmlibrary.library.dto.OrderDTO;
import ru.edu.filmlibrary.library.mapper.OrderMapper;
import ru.edu.filmlibrary.library.model.Order;
import ru.edu.filmlibrary.library.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService extends GenericService<Order, OrderDTO> {

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper) {
        super(orderRepository, orderMapper);
    }

    public void rentFilm(final OrderDTO orderDTO) {
        long rentPeriod = orderDTO.getRentPeriod() != null ? orderDTO.getRentPeriod() : 14L;
        orderDTO.setRentDate(LocalDateTime.now());
        orderDTO.setReturned(false);
        orderDTO.setPurchase(false);
        orderDTO.setRentPeriod((int) rentPeriod);
        orderDTO.setReturnDate(LocalDateTime.now().plusDays(rentPeriod));
        orderDTO.setCreatedWhen(LocalDateTime.now());
        orderDTO.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        mapper.toDTO(repository.save(mapper.toEntity(orderDTO)));
    }

    public Page<OrderDTO> listUserRentFilms(final Long id,
                                            final Pageable pageRequest) {
        Page<Order> objects = ((OrderRepository) repository).getOrdersByUserId(id, pageRequest);
        List<OrderDTO> results = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(results, pageRequest, objects.getTotalElements());
    }

    public void returnFilm(final Long id) {
        OrderDTO orderDTO = getOne(id);
        orderDTO.setReturned(true);
        orderDTO.setReturnDate(LocalDateTime.now());
        mapper.toDTO(repository.save(mapper.toEntity(orderDTO)));
    }
}
