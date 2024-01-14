package ru.edu.filmlibrary.library.controller.rest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.filmlibrary.library.dto.AddFilmDTO;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.service.DirectorsService;

@Hidden
@RestController
@RequestMapping("/api/rest/directors")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Режиссеры", description = "Контроллер для работы с режиссерами фильмов")
public class DirectorsController extends GenericController<Directors, DirectorsDTO> {

    public DirectorsController(DirectorsService directorsService) {
        super(directorsService);
    }

    @Operation(description = "Добавить фильм к режиссеру")
    @RequestMapping(value = "/addFilm", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorsDTO> addFilm(@RequestParam(value = "filmId") Long filmId,
                                                @RequestParam(value = "directorId") Long directorId) {
        AddFilmDTO addFilmDTO = new AddFilmDTO();
        addFilmDTO.setDirectorId(directorId);
        addFilmDTO.setFilmId(filmId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(((DirectorsService) service)
                        .addFilm(addFilmDTO));
    }
}
