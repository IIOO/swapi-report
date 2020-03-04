package com.aleksander.test.report.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class FilmEntryDto {
    private int filmId;
    private String filmName;
    private int characterId;
    private String characterName;
    private int planetId;
    private String planetName;
}
