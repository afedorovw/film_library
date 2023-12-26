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

    public DirectorsDTO(
            String fio,
            LocalDate date,
            Country country,
            String description,
            List<Long> filmIds
    ) {
        this.directorsFIO = fio;
        this.birthDate = date;
        this.country = country;
        this.description_dir = description;
        this.filmsIds = filmIds;
    }

   /* public DirectorsDTO(Directors directors) {
        this.directorsFIO = directors.getDirectorsFIO();
        this.birthDate = directors.getBirthDate();
        this.country = directors.getCountry();
        1
        this.description_dir = directors.getDescription_dir();
        this.createdBy = directors.getCreatedBy();
        this.createdWhen = directors.getCreatedWhen();
        this.id = directors.getId();
        List<Films> films = directors.getFilms();
        List<Long> filmId = new ArrayList<>();
        films.forEach(b -> filmId.add(b.getId()));
        this.filmsIds = filmId;
        this.isDeleted = false;
    }*/
}
