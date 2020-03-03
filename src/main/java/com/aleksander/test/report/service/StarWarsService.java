package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.Person;
import com.aleksander.test.report.dto.Planet;
import com.aleksander.test.report.dto.response.PeopleResponse;
import com.aleksander.test.report.dto.response.PlanetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class StarWarsService {
    private final static String PLANETS_URL = "https://swapi.co/api/planets/";
    private final static String PEOPLE_URL = "https://swapi.co/api/people/";

    private final ApiConsumer apiConsumer;


    public List<Planet> getPlanetsByPhrase(String phrase) {
        List<Planet> planets = new ArrayList<>();
        String url = buildQueryUrl(PLANETS_URL, phrase);

        do {
            PlanetResponse response = apiConsumer.getResponse(url, PlanetResponse.class);
            url = Objects.nonNull(response.getNext()) ? buildQueryUrl(response.getNext()) : null;
            planets.addAll(response.getResults());
        } while (Objects.nonNull(url));

        return planets;
    }

    public List<Person> getPeopleByPhrase(String phrase) {
        List<Person> people = new ArrayList<>();
        String url = buildQueryUrl(PEOPLE_URL, phrase);

        do {
            PeopleResponse response = apiConsumer.getResponse(url, PeopleResponse.class);
            url = Objects.nonNull(response.getNext()) ? buildQueryUrl(response.getNext()) : null;
            people.addAll(response.getResults());
        } while (Objects.nonNull(url));

        return people;
    }

    private String buildQueryUrl(String endpoint) {
        return UriComponentsBuilder.fromUriString(endpoint).toUriString();
    }

    private String buildQueryUrl(String endpoint, String phrase) {
        return UriComponentsBuilder.fromUriString(endpoint).queryParam("search", phrase).toUriString();
    }
}
