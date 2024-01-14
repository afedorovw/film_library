package ru.edu.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.edu.filmlibrary.library.constants.Errors;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.service.FilmsService;

import java.util.List;

@RestController
@RequestMapping("/api/rest/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
public class FilmsController extends GenericController<Films, FilmsDTO> {

    public FilmsController(FilmsService service) {
        super(service);
    }

    @Operation(description = "Добавить фильм к редиссеру")
    @RequestMapping(value = "/addDirector", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmsDTO> addDirector(@RequestParam(value = "filmId") Long filmId,
                                                @RequestParam(value = "directorId") Long directorId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(((FilmsService) service)
                        .addDirector(filmId, directorId));
    }
}
