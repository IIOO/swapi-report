package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.PlanetResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Run with application context to setup thread pool and run async
 */
@SpringBootTest
public class StarWarsServiceAsyncTests {
    @Autowired
    private StarWarsService starWarsService;

    @Test
    void getFilmsByUriAsync() {
//      given
        URI uri1 = URI.create("https://swapi.co/api/films/2/");
        URI uri2 = URI.create("https://swapi.co/api/films/6/");
        URI uri3 = URI.create("https://swapi.co/api/films/3/");
        Set<URI> uriSet = new HashSet<>(Arrays.asList(uri1, uri2, uri3));
//      when
        Map<URI, String> films = starWarsService.getFilmsByUris(uriSet);
//      then
        assertEquals(3, films.size());
        assertEquals("The Empire Strikes Back", films.get(uri1));
        assertEquals("Revenge of the Sith", films.get(uri2));
        assertEquals("Return of the Jedi", films.get(uri3));
    }

    @Test
    void getPlanetsByPhraseManyPagesAsync() {
//      when
        List<PlanetResponseDto> planetDtos = starWarsService.getPlanetsByPhrase("a");
//      then
        assertEquals(40, planetDtos.size());
    }
}
