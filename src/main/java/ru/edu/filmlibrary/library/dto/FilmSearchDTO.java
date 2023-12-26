package ru.edu.filmlibrary.library.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.edu.filmlibrary.library.model.Genre;

@Getter
@Setter
@ToString
public class FilmSearchDTO {

    private String filmTitle;
    private String directorsFio;
    private Genre genre;
}
