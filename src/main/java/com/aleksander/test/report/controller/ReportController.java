package com.aleksander.test.report.controller;

import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.ReportDto;
import com.aleksander.test.report.service.ReportService;
import com.aleksander.test.report.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final SearchService searchService;

    private final ReportService reportService;

    @PutMapping("/{report_id}")
    public ResponseEntity<?> put(@PathVariable("report_id") Long reportId,
                                 @RequestBody GenerateReportCriteriaDto criteriaDto) {

        Set<FilmEntryDto> films = searchService.findFilmsByCharacterAndHisHomeworld(criteriaDto);
        reportService.createOrUpdate(reportId, criteriaDto, films);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{report_id}")
    public ResponseEntity<?> delete(@PathVariable("report_id") Long reportId) {
        reportService.delete(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        reportService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> readAll() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("/{report_id}")
    public ResponseEntity<ReportDto> read(@PathVariable("report_id") final Long reportId) {
        Optional<ReportDto> report = reportService.getById(reportId);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
