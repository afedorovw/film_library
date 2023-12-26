package ru.edu.filmlibrary.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.service.FilmsService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
public class FilmsController extends GenericController<Films, FilmsDTO> {

    public FilmsController(FilmsService filmsService) {
        super(filmsService);
    }

    @Operation(description = "Добавить фильм к режиссеру")
    @RequestMapping(value = "/addDirector",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmsDTO> addFilm(@RequestParam(value = "films_id") Long filmsId,
                                            @RequestParam(value = "directors_id") Long directorsId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(((FilmsService) service).addDirector(filmsId, directorsId));
    }

    @Operation(description = "Получить все фильмы у пользователя",
            method = "getAllFilmsByUserId")
    @RequestMapping(value = "/getAllFilmsByUserId",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilmsDTO>> getAllFilmsByUserId(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(((FilmsService) service).getAllFilmsByUserId(userId));
    }
}
