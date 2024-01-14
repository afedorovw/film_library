package ru.edu.filmlibrary.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.edu.filmlibrary.library.dto.AddFilmDTO;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.mapper.DirectorsMapper;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.repository.DirectorsRepository;
import ru.edu.filmlibrary.library.service.DirectorsService;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DirectorServiceTest extends GenericTest<Directors, DirectorsDTO>{

    public DirectorServiceTest() {
        super();
        repository = Mockito.mock(DirectorsRepository.class);
        mapper = Mockito.mock(DirectorsMapper.class);
        service = new DirectorsService((DirectorsRepository) repository, (DirectorsMapper) mapper);

        DirectorTestData.DIRECTOR_1.setDeleted(false);
        DirectorTestData.DIRECTOR_2.setDeleted(false);
        DirectorTestData.DIRECTOR_3.setDeleted(false);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(DirectorTestData.DIRECTOR_LIST);
        Mockito.when(mapper.toDTOs(DirectorTestData.DIRECTOR_LIST)).thenReturn(DirectorTestData.DIRECTOR_DTO_LIST);

        List<DirectorsDTO> directorsDTOS = service.listAll();
        log.info("Testing getAll(): {}", directorsDTOS);
        assertEquals(DirectorTestData.DIRECTOR_LIST.size(), directorsDTOS.size());
    }

    @Order(2)
    @Test
    @Override
    protected void getOne() { //TODO://Negative test to do
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DirectorTestData.DIRECTOR_1));
        Mockito.when(mapper.toDTO(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_DTO_1);

        DirectorsDTO directorsDTODTO = service.getOne(1L);
        log.info("Testing getOne(): {}", directorsDTODTO);
        assertEquals(DirectorTestData.DIRECTOR_DTO_1, directorsDTODTO);
    }

    @Order(3)
    @Test
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(DirectorTestData.DIRECTOR_DTO_1)).thenReturn(DirectorTestData.DIRECTOR_1);
        Mockito.when(mapper.toDTO(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_DTO_1);
        Mockito.when(repository.save(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_1);

        DirectorsDTO directorsDTO = service.create(DirectorTestData.DIRECTOR_DTO_1);
        log.info("Testing create(): {}", directorsDTO);
    }

    @Order(4)
    @Test
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(DirectorTestData.DIRECTOR_DTO_1)).thenReturn(DirectorTestData.DIRECTOR_1);
        Mockito.when(mapper.toDTO(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_DTO_1);
        Mockito.when(repository.save(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_1);

        DirectorsDTO directorsDTO = service.update(DirectorTestData.DIRECTOR_DTO_1);
        log.info("Testing create(): {}", directorsDTO);
    }

    @Order(5)
    @Test
    @Override
    protected void delete() throws MyDeleteException {
        Mockito.when(((DirectorsRepository) repository).checkDirectorForDeletion(1L)).thenReturn(true);
        Mockito.when(((DirectorsRepository) repository).checkDirectorForDeletion(2L)).thenReturn(false);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DirectorTestData.DIRECTOR_1));

        service.deleteSoft(1L);
        assertTrue(DirectorTestData.DIRECTOR_1.isDeleted());
    }

    @Test
    @Override
    protected void restore() {
        DirectorTestData.DIRECTOR_3.setDeleted(true);
        Mockito.when(repository.save(DirectorTestData.DIRECTOR_3)).thenReturn(DirectorTestData.DIRECTOR_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(DirectorTestData.DIRECTOR_3));
        log.info("Tested restore() before: " + DirectorTestData.DIRECTOR_3.isDeleted());
        service.restore(3L);
        log.info("Tested restore() after: " + DirectorTestData.DIRECTOR_3.isDeleted());
        assertFalse(DirectorTestData.DIRECTOR_3.isDeleted());
    }

    @Test
    @Override
    protected void getAllNotDeleted() {
        List<Directors> directors = DirectorTestData.DIRECTOR_LIST.stream().filter(Predicate.not(Directors::isDeleted)).toList();
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(directors);
        Mockito.when(mapper.toDTOs(directors))
                .thenReturn(DirectorTestData.DIRECTOR_DTO_LIST.stream().filter(Predicate.not(DirectorsDTO::isDeleted)).toList());
        List<DirectorsDTO> directorsDTO = service.listAllNotDeleted();
        log.info("Testing getAllNotDeleted(): {}", directorsDTO);
        assertEquals(directors.size(), directorsDTO.size());
    }

    @Test
    void searchDirectors() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "directorFio1"));

        Mockito.when(((DirectorsRepository) repository).searchAllByDirectorsFIOContainingIgnoreCase("directorFio1", pageRequest))
                .thenReturn(new PageImpl<>(DirectorTestData.DIRECTOR_LIST));
        Mockito.when(mapper.toDTOs(DirectorTestData.DIRECTOR_LIST)).thenReturn(DirectorTestData.DIRECTOR_DTO_LIST);

        Page<DirectorsDTO> directorDTOList = ((DirectorsService) service).searchDirectors("directorFio1", pageRequest);
        assertEquals(DirectorTestData.DIRECTOR_DTO_LIST, directorDTOList.getContent());
    }

    @Test
    void addFilm() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(DirectorTestData.DIRECTOR_1));
        Mockito.when(service.getOne(1L)).thenReturn(DirectorTestData.DIRECTOR_DTO_1);
        Mockito.when(repository.save(DirectorTestData.DIRECTOR_1)).thenReturn(DirectorTestData.DIRECTOR_1);

        ((DirectorsService) service).addFilm(new AddFilmDTO(1L, 1L));
        log.info("Testing addFilm(): {}", DirectorTestData.DIRECTOR_DTO_1.getFilmsIds());
        assertTrue(DirectorTestData.DIRECTOR_DTO_1.getFilmsIds().size() >= 1);
    }
}
