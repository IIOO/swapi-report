package com.aleksander.test.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenerateReportCriteriaDto {
    @JsonProperty("query_criteria_character_phrase")
    private String queryCriteriaCharacterPhrase;

    @JsonProperty("query_criteria_planet_name")
    private String queryCriteriaPlanetName;
}
