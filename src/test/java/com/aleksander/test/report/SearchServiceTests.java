package com.aleksander.test.report;

import com.aleksander.test.report.domain.ReportFilmEntry;
import com.aleksander.test.report.domain.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class SearchServiceTests {
    @Autowired
    SearchService searchService;


    @Test
    void getFilmsWhereCharacterWithGivenNameAndHomeworldPlanetAppearedSimple() {
        Set<ReportFilmEntry> reportEntries = searchService.findFilmsByCharacterAndHisHomeworld(GenerateReportCriteriaDto.builder()
                .queryCriteriaPlanetName("Tatooine")
                .queryCriteriaCharacterPhrase("ana")
                .build());

        assertEquals(3, reportEntries.size());
    }

    @Test
    void getFilmsWhereCharacterWithGivenNameAndHomeworldPlanetAppeared() {
        Set<ReportFilmEntry> reportEntries = searchService.findFilmsByCharacterAndHisHomeworld(GenerateReportCriteriaDto.builder()
                .queryCriteriaPlanetName("Alderaan")
                .queryCriteriaCharacterPhrase("ana")
                .build());

        assertEquals(7, reportEntries.size());
    }
}
