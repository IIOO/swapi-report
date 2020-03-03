package com.aleksander.test.report.service;

import com.aleksander.test.report.domain.ReportFilmEntry;
import com.aleksander.test.report.domain.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.domain.dto.PersonDto;
import com.aleksander.test.report.domain.dto.PlanetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StarWarsService starWarsService;


    public Set<ReportFilmEntry> findFilmsByCharacterAndHisHomeworld(GenerateReportCriteriaDto dto) {
        List<PlanetDto> planets = starWarsService.getPlanetsByPhrase(dto.getQueryCriteriaPlanetName());
        List<PersonDto> people = starWarsService.getPeopleByPhrase(dto.getQueryCriteriaCharacterPhrase());

        List<PersonDto> peopleWithMatchingHomeworld = findPeopleWithHomeworldOnPlanets(people, planets);

        Set<URI> filmsToFetch = getDistinctFilmUris(peopleWithMatchingHomeworld);
        Map<URI, String> films = starWarsService.getFilmsByUris(filmsToFetch);

        Set<ReportFilmEntry> result = new HashSet<>();
        for(PersonDto person : peopleWithMatchingHomeworld) {
            for(URI filmUri : person.getFilms()) {
                result.add(ReportFilmEntry.builder()
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

    private String getPlanetNameByUrl(String url, List<PlanetDto> planets) {
        return planets.stream()
                .filter(planet -> planet.getUrl().toString().equals(url))
                .findFirst()
                .get().getName();
    }

    private Set<URI> getDistinctFilmUris(List<PersonDto> people) {
        return people.stream()
                .map(PersonDto::getFilms)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private List<PersonDto> findPeopleWithHomeworldOnPlanets(List<PersonDto> people, List<PlanetDto> planets) {
        Set<String> planetUrls = planets.stream()
                .map(PlanetDto::getUrl)
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
