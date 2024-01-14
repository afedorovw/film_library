package ru.edu.filmlibrary.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.edu.filmlibrary.library.dto.GenericDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.mapper.GenericMapper;
import ru.edu.filmlibrary.library.model.GenericModel;
import ru.edu.filmlibrary.library.repository.GenericRepository;
import ru.edu.filmlibrary.library.service.GenericService;
import ru.edu.filmlibrary.library.service.userdetails.CustomUserDetails;

public abstract class GenericTest<E extends GenericModel, D extends GenericDTO> {

    protected GenericService<E, D> service;
    protected GenericRepository<E> repository;
    protected GenericMapper<E, D> mapper;

    @BeforeEach
    void init() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CustomUserDetails.builder()
                        .username("USER"),
                null,
                null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected abstract void getAll();
    protected abstract void getOne();
    protected abstract void create();
    protected abstract void update();
    protected abstract void delete() throws MyDeleteException;
    protected abstract void restore();
    protected abstract void getAllNotDeleted();
}
