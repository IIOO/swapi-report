package com.aleksander.test.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class ReportDto {
    private long reportId;
    private String queryCriteriaCharacterPhrase;
    private String queryCriteriaPlanetName;
    private Set<FilmEntryDto> result;
}
