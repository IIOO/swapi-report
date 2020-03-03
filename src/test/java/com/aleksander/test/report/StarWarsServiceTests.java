package com.aleksander.test.report;

import com.aleksander.test.report.dto.Person;
import com.aleksander.test.report.dto.Planet;
import com.aleksander.test.report.service.StarWarsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StarWarsServiceTests {
    @Autowired
    private StarWarsService starWarsService;

//  PLANETS
    @Test
    void getPlanetsByPhraseSimple() {
        List<Planet> planets = starWarsService.getPlanetsByPhrase("Tatooine");
        assertEquals(1, planets.size());
        assertEquals("Tatooine", planets.get(0).getName());
    }

    @Test
    void getPlanetsByPhraseManyPages() {
        List<Planet> planets = starWarsService.getPlanetsByPhrase("a");
        assertEquals(40, planets.size());
    }

    @Test
    void getPlanetsByPhraseNotExisting() {
        List<Planet> planet = starWarsService.getPlanetsByPhrase("pluton");
        assertEquals(0, planet.size());
    }

//  PEOPLE
    @Test
    void getPeopleByPhraseSimple() {
        List<Person> people = starWarsService.getPeopleByPhrase("Luke");
        assertEquals(1, people.size());
        assertEquals("Luke Skywalker", people.get(0).getName());
    }

    @Test
    void getPeopleByPhraseManyPages() {
        List<Person> people = starWarsService.getPeopleByPhrase("an");
        assertEquals(12, people.size());
    }

    @Test
    void getPeopleByPhraseNotExisting() {
        List<Person> people = starWarsService.getPeopleByPhrase("joda");
        assertEquals(0, people.size());
    }
}
