package com.aleksander.test.report.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenerateReportCriteriaDto {
    private String queryCriteriaCharacterPhrase;
    private String queryCriteriaPlanetName;
}
