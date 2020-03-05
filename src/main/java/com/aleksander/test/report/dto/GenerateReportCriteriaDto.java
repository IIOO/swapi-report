package com.aleksander.test.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateReportCriteriaDto {
    @JsonProperty("query_criteria_character_phrase")
    private String queryCriteriaCharacterPhrase;

    @JsonProperty("query_criteria_planet_name")
    private String queryCriteriaPlanetName;
}
