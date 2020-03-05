package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.ReportDto;
import com.aleksander.test.report.persistance.ReportEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportServiceTests {
    @Autowired
    private ReportService reportService;

    @Test
    @Order(1)
    void saveNewReport() {
        // given
        GenerateReportCriteriaDto criteria = new GenerateReportCriteriaDto("Luke","Tatooine");

        // when
        ReportEntity report = reportService.createOrUpdate(1L, criteria, buildFilmsForReportEntries(2, 3));

        // then
        assertEquals(1L, report.getId());
        assertEquals(criteria.getQueryCriteriaPlanetName(), report.getQueryCriteriaPlanetName());
        assertEquals(criteria.getQueryCriteriaCharacterPhrase(), report.getQueryCriteriaCharacterPhrase());
        assertEquals(6, report.getResult().size());
    }

    @Test
    @Order(2)
    void saveReportWithExistingId() {
        // given
        GenerateReportCriteriaDto criteriaUpdated = new GenerateReportCriteriaDto("Obi-Wan Kenobi","Stewjon");

        // when
        ReportEntity reportUpdated = reportService.createOrUpdate(1L, criteriaUpdated, buildFilmsForReportEntries(1, 2));

        // then
        assertEquals(1L, reportUpdated.getId());
        assertEquals(criteriaUpdated.getQueryCriteriaPlanetName(), reportUpdated.getQueryCriteriaPlanetName());
        assertEquals(criteriaUpdated.getQueryCriteriaCharacterPhrase(), reportUpdated.getQueryCriteriaCharacterPhrase());
        assertEquals(2, reportUpdated.getResult().size());
    }

    @Test
    @Order(3)
    void getReportById() {
        // when
        Optional<ReportDto> report = reportService.getById(1L);

        // then
        assertTrue(report.isPresent());
        assertEquals(1L, report.get().getReportId());
    }

    @Test
    @Order(4)
    void getAllReports() {
        // given
        GenerateReportCriteriaDto criteria = new GenerateReportCriteriaDto("Luke","Tatooine");
        reportService.createOrUpdate(2L, criteria, buildFilmsForReportEntries(1, 2));
        reportService.createOrUpdate(3L, criteria, buildFilmsForReportEntries(3, 1));

        // when
        List<ReportDto> reports = reportService.getAll();

        // then
        assertEquals(3, reports.size());
    }

    @Test
    @Order(5)
    void deleteReport() {
        // when
        reportService.delete(3L);

        // then
        assertEquals(2, reportService.getAll().size());
    }

    @Test
    @Order(6)
    void deleteAllReports() {
        // when
        reportService.deleteAll();

        // then
        assertEquals(0, reportService.getAll().size());
    }

    private Set<FilmEntryDto> buildFilmsForReportEntries(int noOfCharacters, int noOfFilms) {
        Set<FilmEntryDto> result = new HashSet<>();
        for(int i = 0; i < noOfCharacters; i++) {
            for(int j = 0; j < noOfFilms; j++) {
                result.add(FilmEntryDto.builder()
                        .filmId(j)
                        .filmName("film " + j)
                        .characterId(i)
                        .characterName("person " + i)
                        .planetId(1)
                        .planetName("ziemia")
                        .build());
            }
        }
        return result;
    }
}
