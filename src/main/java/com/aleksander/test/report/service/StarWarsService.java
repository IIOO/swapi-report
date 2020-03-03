package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.Person;
import com.aleksander.test.report.dto.Planet;
import com.aleksander.test.report.dto.response.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StarWarsService {
    private final static String PLANETS_URL = "https://swapi.co/api/planets/";
    private final static String PEOPLE_URL = "https://swapi.co/api/people/";

    private final ApiConsumer apiConsumer;

    private final ObjectMapper objectMapper;


    public List<Planet> getPlanetsByPhrase(String phrase) {
        return getByPhrase(PLANETS_URL, phrase, Planet.class);
    }

    public List<Person> getPeopleByPhrase(String phrase) {
        return getByPhrase(PEOPLE_URL, phrase, Person.class);
    }

    /**
     * Make get request to endpoint with search query based on phrase to fetch data from all pages
     * @param endpoint
     * @param phrase
     * @param type of object to map
     * @param <T>
     * @return list of all objects matching given query phrase
     */
    private <T> List<T> getByPhrase(String endpoint, String phrase, Class<T> type) {
        List<T> list = new ArrayList<>();
        String url = buildQueryUrl(endpoint, phrase);

        do {
            QueryResponse response = apiConsumer.getResponse(url, QueryResponse.class);
            url = Objects.nonNull(response.getNext()) ? buildQueryUrl(response.getNext()) : null;
            list.addAll(mapToListOfObjectType(response.getResults(), type));
        } while (Objects.nonNull(url));

        return list;
    }

    /**
     * Map list containing LinkedHashMap<String, String> to object of given type
     * @param fromList
     * @param type
     * @param <T>
     * @return mapped list
     */
    private <T> List<T> mapToListOfObjectType(List<Object> fromList, Class<T> type) {
        return fromList.stream()
                .map(ele -> objectMapper.convertValue(ele, type))
                .collect(Collectors.toList());
    }

    private String buildQueryUrl(String endpoint) {
        return UriComponentsBuilder.fromUriString(endpoint).toUriString();
    }

    private String buildQueryUrl(String endpoint, String phrase) {
        return UriComponentsBuilder.fromUriString(endpoint).queryParam("search", phrase).toUriString();
    }
}
