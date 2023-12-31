package ru.edu.filmlibrary.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "directors",
        uniqueConstraints = @UniqueConstraint(name = "uniqueDirectorsName", columnNames = "directors_fio"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator",
        sequenceName = "directors_sequence",
        allocationSize = 1)

public class Directors extends GenericModel {

    @Column(name = "directors_fio", nullable = false)
    private String directorsFIO;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "country")
    @Enumerated
    private Country country;

    @Column(name = "directors_bio")
    private String description_dir;

    @Column(name = "photo")
    private String photo;

    //    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_directors",
            joinColumns = @JoinColumn(name = "director_id"),
            foreignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"),
            inverseJoinColumns = @JoinColumn(name = "film_id"),
            inverseForeignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS")
    )
    private List<Films> films;

}
