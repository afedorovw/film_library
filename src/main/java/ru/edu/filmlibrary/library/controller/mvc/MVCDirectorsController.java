package ru.edu.filmlibrary.library.controller.mvc;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.edu.filmlibrary.library.dto.AddFilmDTO;
import ru.edu.filmlibrary.library.dto.DirectorSearchDTO;
import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.exception.MyDeleteException;
import ru.edu.filmlibrary.library.service.DirectorsService;
import ru.edu.filmlibrary.library.service.FilmsService;


import static ru.edu.filmlibrary.library.constants.UserRolesConstants.ADMIN;

@Slf4j
@Controller
@Hidden
@RequestMapping("/directors")
public class MVCDirectorsController {

    private final DirectorsService directorsService;
    private final FilmsService filmsService;

    public MVCDirectorsController(DirectorsService directorsService,
                                  FilmsService filmsService) {
        this.directorsService = directorsService;
        this.filmsService = filmsService;
    }

    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(
                page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorsFIO"));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<DirectorsDTO> result;
        if (ADMIN.equalsIgnoreCase(userName)) {
            result = directorsService.listAll(pageRequest);
        } else {
            result = directorsService.listAllNotDeleted(pageRequest);
        }
        model.addAttribute("directors", result);
        return "directors/viewAllDirectors";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id,
                         Model model) {
        model.addAttribute("director", directorsService.getOne(id));
        return "directors/viewDirector";
    }

    @GetMapping("/add")
    public String create() {
        return "directors/addDirector";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorsDTO directorsDTO) {
        log.info(directorsDTO.toString());
        directorsService.create(directorsDTO);
        return "redirect:/directors";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("director", directorsService.getOne(id));
        return "directors/updateDirector";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorsDTO directorsDTO) {
        directorsService.update(directorsDTO);
        return "redirect:/directors";
    }

    @GetMapping("/add-film/{directorId}")
    public String addFilm(@PathVariable Long directorId,
                          Model model) {
        model.addAttribute("films", filmsService.listAll());
        model.addAttribute("directorId", directorId);
        model.addAttribute("director", directorsService.getOne(directorId).getDirectorsFIO());
        return "directors/addDirectorFilm";
    }

    @PostMapping("/add-film")
    public String addFilm(@ModelAttribute("directorFilmForm") AddFilmDTO addFilmDTO) {
        directorsService.addFilm(addFilmDTO);
        return "redirect:/directors";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        directorsService.deleteSoft(id);
        return "redirect:/directors";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        directorsService.restore(id);
        return "redirect:/directors";
    }

    @PostMapping("/search")
    public String searchDirectors(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                  @ModelAttribute("directorSearchForm") DirectorSearchDTO directorsDTO,
                                  Model model) {
        if (StringUtils.hasText(directorsDTO.getDirectorsFIO()) ||
                StringUtils.hasLength(directorsDTO.getDirectorsFIO())) {
            PageRequest pageRequest = PageRequest.of(
                    page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorsFIO"));
            model.addAttribute("directors",
                    directorsService.searchDirectors(directorsDTO.getDirectorsFIO().trim(), pageRequest));
            return "directors/viewAllDirectors";
        } else return "redirect:/directors";
    }
}
