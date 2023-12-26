package ru.edu.filmlibrary.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.edu.filmlibrary.library.model.Genre;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FilmsDTO extends GenericDTO {

    private String title;
    private Long premierYear;
    private String country;
    private Genre genre;
    private List<Long> directorsId;
    private List<Long> filmOrderId;

}
