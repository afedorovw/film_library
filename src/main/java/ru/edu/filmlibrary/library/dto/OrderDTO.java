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
    private Long rentPeriod;
    private boolean purchase;
    private Long filmId;
    private Long userId;
}
