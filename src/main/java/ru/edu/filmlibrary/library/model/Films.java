package ru.edu.filmlibrary.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "films_sequence", allocationSize = 1)

public class Films extends GenericModel {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "premier_year", nullable = false)
    private Long premierYear;

    @Column(name = "genre")
    @Enumerated
    private Genre genre;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rent_price")
    private Integer rentPrice;

    @Column(name = "online_copy_path")
    private String onlineCopyPath;

    @Column(name = "video")
    private String video;

    @Column(name = "stars")
    private Double stars;

//    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_directors",
            joinColumns = @JoinColumn(name = "film_id"), foreignKey = @ForeignKey(name = "FK_FILMS_DIRECTORS"),
            inverseJoinColumns = @JoinColumn(name = "director_id"), inverseForeignKey = @ForeignKey(name = "FK_DIRECTORS_FILMS"))
    List<Directors> directors;

    @OneToMany(mappedBy = "film")
    private List<Order> orders;

}
