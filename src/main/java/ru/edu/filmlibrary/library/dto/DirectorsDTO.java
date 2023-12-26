package ru.edu.filmlibrary.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class DirectorsDTO extends GenericDTO {

    private String directorsFIO;
    private String country;
    private String position;
    private List<Long> filmsIds;
}
