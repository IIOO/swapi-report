package com.aleksander.test.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private long reportId;
    private String queryCriteriaCharacterPhrase;
    private String queryCriteriaPlanetName;
    private Set<FilmEntryDto> result;
}
