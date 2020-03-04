package com.aleksander.test.report.repository;

import com.aleksander.test.report.dto.ReportDto;
import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.persistance.FilmEntryEntity;
import com.aleksander.test.report.persistance.ReportEntity;

import java.util.stream.Collectors;

public class ReportMapper {
    public static ReportDto mapReportEntityToReportDto(ReportEntity entity) {
        return ReportDto.builder()
                .reportId(entity.getId())
                .queryCriteriaPlanetName(entity.getQueryCriteriaPlanetName())
                .queryCriteriaCharacterPhrase(entity.getQueryCriteriaCharacterPhrase())
                .result(entity.getResult().stream()
                        .map(ReportMapper::mapFilmEntryEntityToFilmEntryDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static FilmEntryDto mapFilmEntryEntityToFilmEntryDto(FilmEntryEntity entity) {
        return FilmEntryDto.builder()
                .characterId(entity.getCharacterId())
                .characterName(entity.getCharacterName())
                .planetId(entity.getPlanetId())
                .planetName(entity.getPlanetName())
                .filmId(entity.getFilmId())
                .filmName(entity.getFilmName())
                .build();
    }

    public static FilmEntryEntity mapFilmEntryDtoToFilmEntryEntity(FilmEntryDto dto) {
        return FilmEntryEntity.builder()
                .filmId(dto.getFilmId())
                .filmName(dto.getFilmName())
                .characterId(dto.getCharacterId())
                .characterName(dto.getCharacterName())
                .planetId(dto.getPlanetId())
                .planetName(dto.getPlanetName())
                .build();
    }
}
