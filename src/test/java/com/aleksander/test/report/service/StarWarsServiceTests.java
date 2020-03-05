package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.PersonResponseDto;
import com.aleksander.test.report.dto.response.PlanetResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StarWarsServiceTests {

    private static StarWarsService starWarsService;

    @BeforeAll
    static void setUp() {
        ApiConsumer apiConsumer = new ApiConsumer(new RestTemplate());
        ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        starWarsService = new StarWarsService(apiConsumer, objectMapper);
    }


//  PLANETS
    @Test
    void getPlanetsByPhraseSimple() {
        // when
        List<PlanetResponseDto> planetDtos = starWarsService.getPlanetsByPhrase("Tatooine");
        // then
        assertEquals(1, planetDtos.size());
        assertEquals("Tatooine", planetDtos.get(0).getName());
    }

    @Test
    void getPlanetsByPhraseManyPages() {
        // when
        List<PlanetResponseDto> planetDtos = starWarsService.getPlanetsByPhrase("a");
        // then
        assertEquals(40, planetDtos.size());
    }

    @Test
    void getPlanetsByPhraseNotExisting() {
        // when
        List<PlanetResponseDto> planetDto = starWarsService.getPlanetsByPhrase("pluton");
        // then
        assertEquals(0, planetDto.size());
    }

//  PEOPLE
    @Test
    void getPeopleByPhraseSimple() {
        // when
        List<PersonResponseDto> people = starWarsService.getPeopleByPhrase("Luke");
        // then
        assertEquals(1, people.size());
        assertEquals("Luke Skywalker", people.get(0).getName());
    }

    @Test
    void getPeopleByPhraseManyPages() {
        // when
        List<PersonResponseDto> people = starWarsService.getPeopleByPhrase("an");
        // then
        assertEquals(12, people.size());
    }

    @Test
    void getPeopleByPhraseNotExisting() {
        // when
        List<PersonResponseDto> people = starWarsService.getPeopleByPhrase("joda");
        // then
        assertEquals(0, people.size());
    }

//  FILMS
    @Test
    void getFilmsByUri() {
        // given
        URI uri1 = URI.create("https://swapi.co/api/films/2/");
        URI uri2 = URI.create("https://swapi.co/api/films/6/");
        URI uri3 = URI.create("https://swapi.co/api/films/3/");
        Set<URI> uriSet = new HashSet<>(Arrays.asList(uri1, uri2, uri3));
        // when
        Map<URI, String> films = starWarsService.getFilmsByUris(uriSet);
        // then
        assertEquals(3, films.size());
        assertEquals("The Empire Strikes Back", films.get(uri1));
        assertEquals("Revenge of the Sith", films.get(uri2));
        assertEquals("Return of the Jedi", films.get(uri3));
    }
}
