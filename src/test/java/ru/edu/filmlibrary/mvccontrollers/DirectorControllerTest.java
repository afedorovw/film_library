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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.model.Country;
import ru.edu.filmlibrary.library.service.DirectorsService;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Slf4j
@Transactional
@Rollback(value = false)
public class DirectorControllerTest extends CommonTestMVC {

    @Autowired
    private DirectorsService directorService;

    private final DirectorsDTO directorDTO = new DirectorsDTO(
            "Test_Director",
            LocalDate.of(1971, 2, 21),
            Country.UNITED_KINGDOM,
            "MVC_descTest",
            new ArrayList<>()
    );

    private final DirectorsDTO directorDTOUpdated = new DirectorsDTO(
            "Test_DirectorUpd",
            LocalDate.of(1986, 7, 12),
            Country.USA,
            "MVC_descUpd",
            new ArrayList<>()
    );

    @Override
    @Test
    @DisplayName("Просмотр всех режиссеров через MVC контроллер")
    @Order(0)
    @WithAnonymousUser
    protected void listAll() throws Exception {
        log.info("Тест просмотра режиссеров MVC начат!");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/directors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/viewAllDirectors"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }

    @Override
    @Test
    @Order(1)
    @DisplayName("Создание режиссера через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        log.info("Тест по созданию режиссера через MVC начат");
        mvc.perform(MockMvcRequestBuilders.post("/directors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("directorForm", directorDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrlTemplate("/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по созданию режиссера через MVC закончен!");
    }

    @Order(2)
    @Test
    @DisplayName("Обновление режиссера через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению режиссера через MVC начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorsFIO"));
        DirectorsDTO foundDirectorForUpdate = directorService.searchDirectors(directorDTO.getDirectorsFIO(), pageRequest).getContent().get(0);
        foundDirectorForUpdate.setDirectorsFIO(directorDTOUpdated.getDirectorsFIO());
        mvc.perform(post("/directors/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("directorForm", foundDirectorForUpdate)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));
        log.info("Тест по обновлению режиссера через MVC закончен успешно");
    }

    @Order(3)
    @Test
    @DisplayName("Софт удаление режиссера через MVC контроллер, тестирование 'directors/delete'")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    @Override
    protected void deleteObject() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "directorsFIO"));
        DirectorsDTO foundDirectorForDelete = directorService.searchDirectors(directorDTOUpdated.getDirectorsFIO(), pageRequest).getContent().get(0);
        foundDirectorForDelete.setDeleted(true);
        mvc.perform(get("/directors/delete/{id}", foundDirectorForDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrl("/directors"));

        DirectorsDTO deletedDirector = directorService.getOne(foundDirectorForDelete.getId());
        assertTrue(deletedDirector.isDeleted());
        log.info("Тест по soft удалению режиссера через MVC закончен успешно!");
    }
}
