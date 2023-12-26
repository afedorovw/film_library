package ru.edu.filmlibrary.library.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO extends GenericDTO {

    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private Integer rentPeriod;
    private Boolean purchase;
    private Boolean endRent;
    private Boolean returned;
    private Long filmId;
    private Long userId;
    private FilmsDTO filmsDTO;
}
