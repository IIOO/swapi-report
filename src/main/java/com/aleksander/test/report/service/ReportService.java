package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.ReportDto;
import com.aleksander.test.report.persistance.ReportEntity;
import com.aleksander.test.report.repository.ReportMapper;
import com.aleksander.test.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;


    public ReportEntity createOrUpdate(Long reportId, GenerateReportCriteriaDto criteriaDto, Set<FilmEntryDto> newFilmEntries) {
        ReportEntity newReport = ReportEntity.builder()
                .id(reportId)
                .queryCriteriaCharacterPhrase(criteriaDto.getQueryCriteriaCharacterPhrase())
                .queryCriteriaPlanetName(criteriaDto.getQueryCriteriaPlanetName())
                .result(newFilmEntries.stream()
                        .map(ReportMapper::mapFilmEntryDtoToFilmEntryEntity)
                        .collect(Collectors.toSet()))
                .build();
        return reportRepository.save(newReport);
    }

    public void delete(Long reportId) {
        reportRepository.deleteById(reportId);
    }

    public void deleteAll() {
        reportRepository.deleteAll();
    }

    public Optional<ReportDto> getById(Long reportId) {
        return reportRepository.findById(reportId)
                .map(ReportMapper::mapReportEntityToReportDto);
    }

    public List<ReportDto> getAll() {
        return reportRepository.findAll().stream()
                .map(ReportMapper::mapReportEntityToReportDto)
                .collect(Collectors.toList());
    }
}
