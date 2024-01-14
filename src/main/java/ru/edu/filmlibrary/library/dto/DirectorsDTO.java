package ru.edu.filmlibrary.library.dto;

import lombok.*;
import ru.edu.filmlibrary.library.model.Country;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.Films;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DirectorsDTO extends GenericDTO {

    private String directorsFIO;
    private LocalDate birthDate;
    private Country country;
    private String description_dir;
    private String photo;
    private List<Long> filmsIds;

    public DirectorsDTO(Directors directors) {
        this.birthDate = directors.getBirthDate();
        this.createdBy = directors.getCreatedBy();
        this.directorsFIO = directors.getDirectorsFIO();
        this.description_dir = directors.getDescription_dir();
        this.createdWhen = directors.getCreatedWhen();
        this.id = directors.getId();
        List<Films> films = directors.getFilms();
        List<Long> filmsIds = new ArrayList<>();
        films.forEach(b -> filmsIds.add(b.getId()));
        this.filmsIds = filmsIds;
        this.isDeleted = false;
    }

    public DirectorsDTO(String fio, LocalDate date, String description, List<Long> filmIds) {
        this.directorsFIO = fio;
        this.birthDate = date;
        this.description_dir = description;
        this.filmsIds = filmIds;
    }
}
