package ru.edu.filmlibrary.library.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.dto.FilmSearchDTO;
import ru.edu.filmlibrary.library.dto.FilmsDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.service.FilmsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/films")
public class MVCFilmsController {

    private final FilmsService filmsService;

    public MVCFilmsController(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(
                page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<FilmsDTO> filmsDTOS = filmsService.getAllFilms(pageRequest);
        model.addAttribute("films", filmsDTOS);
        model.addAttribute("exception", exception);
        return "films/viewAllFilms";
    }

    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmsDTO newFilm) {
        log.info(newFilm.toString());
        filmsService.create(newFilm);
        return "redirect:/films";
    }

    //TODO: добавление фильма с файлом
    /*@PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmsDTO newFilm,
                         @RequestParam("onlineCopy") MultipartFile file) {
        log.info(newFilm.toString());
        if (file != null && file.getSize() > 0) {
            log.info(file.getName());
            filmsService.create(newFilm, file);
        } else filmsService.create(newFilm);
        return "redirect:/films";
    }*/

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmsService.getOne(id));
        return "films/viewFilm";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("film", filmsService.getOne(id));
        return "films/updateFilm";
    }

    @PostMapping
    public String update(@ModelAttribute("filmForm") FilmsDTO filmsDTO) {
        filmsService.update(filmsDTO);
        return "redirect:/films";
    }

    @GetMapping("/setStar/{id}")
    public String setStar(@PathVariable Long id,
                          Model model) {
        FilmsDTO filmsDTO = filmsService.getOne(id);
        model.addAttribute("star", filmsDTO);
        return "error/work";
    }

    @PostMapping("/setStar")
    public String setStar(@ModelAttribute("starForm") FilmsDTO filmsDTO) {
        filmsService.setStars(filmsDTO);
        return "redirect:/films";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        filmsService.deleteSoft(id);
        return "redirect:/films";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        filmsService.restore(id);
        return "redirect:/films";
    }

    @PostMapping("/search")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("filmSearchForm") FilmSearchDTO filmSearchDTO,
                              Model model) {
        if (StringUtils.hasText(filmSearchDTO.getFilmTitle()) ||
                StringUtils.hasLength(filmSearchDTO.getFilmTitle())) {
            PageRequest pageRequest = PageRequest.of(
                    page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
            model.addAttribute("films",
                    filmsService.searchFilm(filmSearchDTO.getFilmTitle().trim(), pageRequest));
            return "films/viewAllFilms";
        } else return "redirect:/films";
    }

    @PostMapping("/search/filmsByDirector")
    public String searchFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              @ModelAttribute("directorSearchForm") DirectorsDTO directorsDTO,
                              Model model) {
        FilmSearchDTO filmSearchDTO = new FilmSearchDTO();
        filmSearchDTO.setDirectorsFio(directorsDTO.getDirectorsFIO());
        return searchFilms(page, pageSize, filmSearchDTO, model);
    }

    @GetMapping(value = "/download", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFilm(@Param(value = "filmId") Long filmId)
            throws IOException {
        FilmsDTO filmsDTO = filmsService.getOne(filmId);
        Path path = Paths.get(filmsDTO.getOnlineCopyPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(createHeaders(path.getFileName().toString()))
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private HttpHeaders createHeaders(final String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        headers.add("Cache-Control", "no-cache, no-store");
        headers.add("Expires", "0");
        return headers;
    }

    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class, NotFoundException.class})
    public RedirectView handleError(HttpServletRequest request,
                                    Exception exception,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос " + request.getRequestURL() + " вызвал ошибку: " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return new RedirectView("/films", true);
    }

}
