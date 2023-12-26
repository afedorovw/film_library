package ru.edu.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.edu.filmlibrary.library.model.Order;

@Repository
public interface OrderRepository extends GenericRepository<Order>{

    Page<Order> getOrdersByUserId (Long id, Pageable pageRequest);
}
