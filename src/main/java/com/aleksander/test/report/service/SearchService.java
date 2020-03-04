package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.response.PersonResponseDto;
import com.aleksander.test.report.dto.response.PlanetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final StarWarsService starWarsService;

    public Set<FilmEntryDto> findFilmsByCharacterAndHisHomeworld(GenerateReportCriteriaDto dto) {
        List<PlanetResponseDto> planets = starWarsService.getPlanetsByPhrase(dto.getQueryCriteriaPlanetName());
        List<PersonResponseDto> people = starWarsService.getPeopleByPhrase(dto.getQueryCriteriaCharacterPhrase());

        List<PersonResponseDto> peopleWithMatchingHomeworld = findPeopleWithHomeworldOnPlanets(people, planets);

        Set<URI> filmsToFetch = getDistinctFilmUris(peopleWithMatchingHomeworld);
        Map<URI, String> films = starWarsService.getFilmsByUris(filmsToFetch);

        return buildFilmEntriesResponse(peopleWithMatchingHomeworld, planets, films);
    }

    private Set<FilmEntryDto> buildFilmEntriesResponse(List<PersonResponseDto> peopleWithMatchingHomeworld,
                                                       List<PlanetResponseDto> planets,
                                                       Map<URI, String> films) {
        Set<FilmEntryDto> result = new HashSet<>();
        for(PersonResponseDto person : peopleWithMatchingHomeworld) {
            for(URI filmUri : person.getFilms()) {
                result.add(FilmEntryDto.builder()
                        .filmId(getIdFromUrl(filmUri))
                        .filmName(films.get(filmUri))
                        .characterId(getIdFromUrl(person.getUrl()))
                        .characterName(person.getName())
                        .planetId(getIdFromUrl(person.getHomeworld()))
                        .planetName(getPlanetNameByUrl(person.getHomeworld(), planets))
                        .build());
            }
        }
        return result;
    }


    private String getPlanetNameByUrl(String url, List<PlanetResponseDto> planets) {
        return planets.stream()
                .filter(planet -> planet.getUrl().toString().equals(url))
                .findFirst()
                .get().getName();
    }

    private Set<URI> getDistinctFilmUris(List<PersonResponseDto> people) {
        return people.stream()
                .map(PersonResponseDto::getFilms)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private List<PersonResponseDto> findPeopleWithHomeworldOnPlanets(List<PersonResponseDto> people, List<PlanetResponseDto> planets) {
        Set<String> planetUrls = planets.stream()
                .map(PlanetResponseDto::getUrl)
                .map(URI::toString)
                .collect(Collectors.toSet());

        return people.stream()
                .filter(person -> planetUrls.contains(person.getHomeworld()))
                .collect(Collectors.toList());
    }

    private static Integer getIdFromUrl(String uri) {
        return Integer.valueOf(uri.replaceAll("[^0-9]", ""));
    }

    private static Integer getIdFromUrl(URI uri) {
        return getIdFromUrl(uri.toString());
    }
}
