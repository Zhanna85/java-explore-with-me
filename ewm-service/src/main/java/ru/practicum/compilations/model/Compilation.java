package ru.practicum.compilations.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "compilations")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@DynamicUpdate
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;  // Идентификатор;

    @Column(name = "pinned", nullable = false, columnDefinition = "true")
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта;

    @Column(name = "title", nullable = false)
    private String title; // Заголовок подборки.
}