package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.FilmEntryDto;
import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.response.PersonResponseDto;
import com.aleksander.test.report.dto.response.PlanetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTests {
    private static String FILM_BASE_URL = "https://swapi.co/api/films/";

    private SearchService searchService;

    @Mock
    private StarWarsService starWarsService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService(starWarsService);
    }


    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void getFilmsWhereCharacterWithGivenNameAndHomeworldPlanetAppearedSimple() {
//      given
        GenerateReportCriteriaDto phrases = GenerateReportCriteriaDto.builder()
                .queryCriteriaPlanetName("Tatooine")
                .queryCriteriaCharacterPhrase("ana")
                .build();

        Mockito.when(starWarsService.getPeopleByPhrase(phrases.getQueryCriteriaCharacterPhrase()))
                .thenReturn(buildPeopleListSimple());
        Mockito.when(starWarsService.getPlanetsByPhrase(phrases.getQueryCriteriaPlanetName()))
                .thenReturn(buildPlanetListSimple());
        Mockito.when(starWarsService.getFilmsByUris(
                new HashSet<>(Arrays.asList(
                        buildUriFromId(FILM_BASE_URL, 5),
                        buildUriFromId(FILM_BASE_URL, 4),
                        buildUriFromId(FILM_BASE_URL, 6)
                )))
        ).thenReturn(buildFilmsMapSimple());

//      when
        Set<FilmEntryDto> reportEntries = searchService.findFilmsByCharacterAndHisHomeworld(GenerateReportCriteriaDto.builder()
                .queryCriteriaCharacterPhrase("ana")
                .queryCriteriaPlanetName("Tatooine")
                .build());
//      then
        assertEquals(3, reportEntries.size());
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    void getFilmsWhereCharacterWithGivenNameAndHomeworldPlanetAppeared() {
//      given
        GenerateReportCriteriaDto phrases = GenerateReportCriteriaDto.builder()
                .queryCriteriaPlanetName("Alderaan")
                .queryCriteriaCharacterPhrase("ana")
                .build();

        Mockito.when(starWarsService.getPeopleByPhrase(phrases.getQueryCriteriaCharacterPhrase())).thenReturn(buildPeopleListSimple());
        Mockito.when(starWarsService.getPlanetsByPhrase(phrases.getQueryCriteriaPlanetName())).thenReturn(buildPlanetList());
        Mockito.when(starWarsService.getFilmsByUris(
                new HashSet<>(Arrays.asList(
                        buildUriFromId(FILM_BASE_URL, 2),
                        buildUriFromId(FILM_BASE_URL, 5),
                        buildUriFromId(FILM_BASE_URL, 7),
                        buildUriFromId(FILM_BASE_URL, 3),
                        buildUriFromId(FILM_BASE_URL, 6),
                        buildUriFromId(FILM_BASE_URL, 1)
                )))
        ).thenReturn(buildFilmsMap());

//      when
        Set<FilmEntryDto> reportEntries = searchService.findFilmsByCharacterAndHisHomeworld(phrases);

//      then
        assertEquals(7, reportEntries.size());
    }

    private List<PlanetResponseDto> buildPlanetListSimple() {
        return Collections.singletonList(new PlanetResponseDto(URI.create("https://swapi.co/api/planets/1/"), "Tatooine"));
    }

    private List<PersonResponseDto> buildPeopleListSimple() {
        return Arrays.asList(
                new PersonResponseDto(buildStringUriFromId(StarWarsService.PLANETS_URL, 2), "Leia Organa", buildUriFromId(StarWarsService.PEOPLE_URL, 5), buildFilmUrisFromId(Arrays.asList(2,6,3,1,7))),
                new PersonResponseDto(buildStringUriFromId(StarWarsService.PLANETS_URL, 1), "Anakin Skywalker", buildUriFromId(StarWarsService.PEOPLE_URL, 11), buildFilmUrisFromId(Arrays.asList(5,4,6))),
                new PersonResponseDto(buildStringUriFromId(StarWarsService.PLANETS_URL, 8), "Quarsh Panaka", buildUriFromId(StarWarsService.PEOPLE_URL, 42), buildFilmUrisFromId(Arrays.asList(4))),
                new PersonResponseDto(buildStringUriFromId(StarWarsService.PLANETS_URL, 2), "Bail Prestor Organa", buildUriFromId(StarWarsService.PEOPLE_URL, 68), buildFilmUrisFromId(Arrays.asList(5,6)))
        );
    }

    private Map<URI, String> buildFilmsMapSimple() {
        HashMap<URI, String> map = new HashMap<>();
        map.put(buildUriFromId(FILM_BASE_URL, 5), "Attack of the Clones");
        map.put(buildUriFromId(FILM_BASE_URL, 4), "The Phantom Menace");
        map.put(buildUriFromId(FILM_BASE_URL, 6), "Revenge of the Sith");
        return map;
    }

    private List<PlanetResponseDto> buildPlanetList() {
        return Collections.singletonList(new PlanetResponseDto(buildUriFromId(StarWarsService.PLANETS_URL, 2), "Alderaan"));
    }

    private Map<URI, String> buildFilmsMap() {
        HashMap<URI, String> map = new HashMap<>();
        map.put(buildUriFromId(FILM_BASE_URL, 2), "The Empire Strikes Back");
        map.put(buildUriFromId(FILM_BASE_URL, 5), "Attack of the Clones");
        map.put(buildUriFromId(FILM_BASE_URL, 7), "The Force Awakens");
        map.put(buildUriFromId(FILM_BASE_URL, 3), "Return of the Jedi");
        map.put(buildUriFromId(FILM_BASE_URL, 6), "Revenge of the Sith");
        map.put(buildUriFromId(FILM_BASE_URL, 1), "A New Hope");
        return map;
    }

    private List<URI> buildFilmUrisFromId(List<Integer> list) {
        return list.stream().map(ele -> buildUriFromId(FILM_BASE_URL, ele)).collect(Collectors.toList());
    }

    private String buildStringUriFromId(String base, int id) {
        return base + id + "/";
    }

    private URI buildUriFromId(String base, int id) {
        return URI.create(base + id + "/");
    }
}
