package ru.edu.filmlibrary.library.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "orders_seq", allocationSize = 1)
public class Order extends GenericModel{

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_ORDER_USER"))
    private Users user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "film_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_ORDER_FILM"))
    private Films film;

    @Column(name = "rentDate", nullable = false)
    private LocalDateTime rentDate;

    @Column(name = "rentPeriod", nullable = false)
    private Integer rentPeriod;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate;

    @Column(name = "purchase", nullable = false)
    private Boolean purchase;

    @Column(name = "end_rent", nullable = false)
    private boolean endRent;

    @Column(name = "returned", nullable = false)
    private Boolean returned;

}
