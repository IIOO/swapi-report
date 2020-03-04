package com.aleksander.test.report.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "film_entry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmEntryEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer filmId;

    @NotNull
    @Length(max = 70)
    private String filmName;

    @NotNull
    private Integer characterId;

    @NotNull
    @Length(max = 50)
    private String characterName;

    @NotNull
    private Integer planetId;

    @NotNull
    @Length(max = 50)
    private String planetName;
}
