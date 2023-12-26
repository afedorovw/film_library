package ru.edu.filmlibrary.library.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddFilmDTO {
    Long filmId;
    Long directorId;
}
