package com.aleksander.test.report.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class ReportFilmEntry {
    private int filmId;
    private String filmName;
    private int characterId;
    private String characterName;
    private int planetId;
    private String planetName;
}
