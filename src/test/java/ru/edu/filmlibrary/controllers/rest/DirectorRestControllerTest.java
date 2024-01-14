package ru.edu.filmlibrary.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Profile("dev")
public class DirectorRestControllerTest extends CommonTestREST{
    private static Long createdTestDirectorId;

    @Test
    @Order(0)
    @Override
    protected void listAll() throws Exception {
        log.info("Тест по просмотру всех режиссеров через REST начат");
        String result = mvc.perform(
                        MockMvcRequestBuilders.get("/api/rest/directors/getAll")
                                .headers(super.headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(greaterThan(0))))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<DirectorsDTO> directorsDTOS = objectMapper.readValue(result, new TypeReference<List<DirectorsDTO>>() {});
        directorsDTOS.forEach(a -> log.info(a.toString()));
        log.info("Тест по просмотру всех режиссеров через REST закончен");
    }

    @Test
    @Order(1)
    @Override
    protected void createObject() throws Exception {
        log.info("Тест по созданию режиссера через REST начат");

        DirectorsDTO directorsDTO = new DirectorsDTO("REST_TestDirectorFIO", LocalDate.now(), "Test Director Description", new ArrayList<>());

        DirectorsDTO result = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.post("/api/rest/directors/add")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .content(asJsonString(directorsDTO))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                DirectorsDTO.class);
        createdTestDirectorId = result.getId();
        log.info("{}", createdTestDirectorId);
        log.info("Тест по созданию режиссера через REST закончен");
    }

    @Test
    @Order(2)
    @Override
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению режиссера через REST начат");
        DirectorsDTO existingTestDirector = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.get("/api/rest/directors/getOneById")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .param("id", String.valueOf(createdTestDirectorId))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                DirectorsDTO.class);
        existingTestDirector.setDirectorsFIO("REST_TestDirectorFIO_UPDATED");
        existingTestDirector.setDescription_dir("REST_TestDirectorDescription_UPDATED");

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/rest/directors/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .content(asJsonString(existingTestDirector))
                                .param("id", String.valueOf(createdTestDirectorId))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        log.info("Тест по обновлению режиссера через REST закончен");
    }

    /*@Test
    @Order(3)
    void addFilm() throws Exception {
        log.info("Тест по добавлению фильма к режиссеру через REST начат");
        DirectorsDTO addFilmDTO = new DirectorsDTO(1L, createdTestDirectorId);
        log.info("addFilmDTO: {}", addFilmDTO);
        String result = mvc.perform(
                        MockMvcRequestBuilders.post("/api/rest/directors/addFilm")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .content(asJsonString(addFilmDTO))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DirectorsDTO directorsDTO = objectMapper.readValue(result, DirectorsDTO.class);
        log.info("Тест по добавлению фильма к режиссеру через REST закончен. Результат: {}", directorsDTO);
    }*/

    @Test
    @Order(4)
    @Override
    protected void deleteObject() throws Exception {
        log.info("Тест по удалению режиссера через REST начат");
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/rest/directors/delete/{id}", createdTestDirectorId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        DirectorsDTO existingTestDirector = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.get("/api/rest/directors/getOneById")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .param("id", String.valueOf(createdTestDirectorId))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                DirectorsDTO.class);
        assertTrue(existingTestDirector.isDeleted());
        log.info("Тест по удалению режиссера через REST закончен");
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/rest/directors/delete/hard/{id}", createdTestDirectorId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        log.info("Данные очищены!");
    }
}
