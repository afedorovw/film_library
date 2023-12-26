package ru.edu.filmlibrary.library.dto;

import lombok.*;
import ru.edu.filmlibrary.library.model.Directors;
import ru.edu.filmlibrary.library.model.Films;
import ru.edu.filmlibrary.library.model.Genre;
import ru.edu.filmlibrary.library.model.Order;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmsDTO extends GenericDTO {

    private String title;
    private Long premierYear;
    private Genre genre;
    private String description;
    private Integer price;
    private Integer rentPrice;
    private String onlineCopyPath;
    private String video;
    private Double stars;
    private List<Long> directorsId;
    private List<Long> filmOrderId;
    private List<DirectorsDTO> directorsInfo;

    public FilmsDTO(
            String title,
            Long year,
            Genre genre,
            String desc
    ) {
        this.title = title;
        this.premierYear = year;
        this.genre = genre;
        this.description = desc;
    }
}
