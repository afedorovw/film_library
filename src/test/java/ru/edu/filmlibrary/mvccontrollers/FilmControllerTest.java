package ru.edu.filmlibrary.mvccontrollers;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.model.Country;
import ru.edu.filmlibrary.library.model.Genre;
import ru.edu.filmlibrary.library.service.FilmsService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@Rollback(value = false)
public class FilmControllerTest extends CommonTestMVC {

    @Autowired
    private FilmsService filmService;

    private final FilmsDTO filmDTO = new FilmsDTO(
            "Test_Film",
            2021L,
            Genre.ACTION,
            "Test_desc"
            );

    private final FilmsDTO filmDTOUpdated = new FilmsDTO(
            "Test_FilmUp",
            2010L,
            Genre.COMEDY,
            "Test_descUp"
    );

    private final DirectorsDTO directorDTO = new DirectorsDTO(
            "Test_DirectorFilm",
            LocalDate.of(1971, 2, 21),
            Country.UNITED_KINGDOM,
            "MVC_descTest",
            new ArrayList<>()
    );

    @Override
    @Test
    @DisplayName("Просмотр всех фильмов через MVC контроллер")
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void listAll() throws Exception {
        log.info("Тест просмотра фильмов MVC начат!");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("films/viewAllFilms"))
                .andExpect(model().attributeExists("films"))
                .andReturn();
    }

    @Override
    @Test
    @Order(1)
    @DisplayName("Создание режиссера через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        log.info("Тест по созданию фильма через MVC начат");
        mvc.perform(MockMvcRequestBuilders.post("/films/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("filmForm", filmDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrlTemplate("/films"))
                .andExpect(redirectedUrl("/films"));
        log.info("Тест по созданию фильма через MVC закончен!");
    }

    protected void updateObject() {
    }

    @Order(2)
    @Test
    @DisplayName("Софт удаление фильма через MVC контроллер, тестирование 'films/delete'")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Override
    protected void deleteObject() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "title"));
        FilmsDTO foundFilmForDelete = filmService.searchFilm(filmDTO.getTitle(), pageRequest).getContent().get(0);
        foundFilmForDelete.setDeleted(true);
        mvc.perform(get("/films/delete/{id}", foundFilmForDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrl("/films"));

        FilmsDTO deletedFilm = filmService.getOne(foundFilmForDelete.getId());
        assertTrue(deletedFilm.isDeleted());
        log.info("Тест по soft удалению фильма через MVC закончен успешно!");
    }
}
