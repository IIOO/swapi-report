package com.aleksander.test.report.service;

import com.aleksander.test.report.domain.dto.FilmDto;
import com.aleksander.test.report.domain.dto.PersonDto;
import com.aleksander.test.report.domain.dto.PlanetDto;
import com.aleksander.test.report.domain.response.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StarWarsService {
    private final static String PLANETS_URL = "https://swapi.co/api/planets/";
    private final static String PEOPLE_URL = "https://swapi.co/api/people/";

    private final ApiConsumer apiConsumer;

    private final ObjectMapper objectMapper;


    public List<PlanetDto> getPlanetsByPhrase(String phrase) {
        return getByPhrase(PLANETS_URL, phrase, PlanetDto.class);
    }

    public List<PersonDto> getPeopleByPhrase(String phrase) {
        return getByPhrase(PEOPLE_URL, phrase, PersonDto.class);
    }

    public Map<URI, String> getFilmsByUris(Set<URI> uris) {
        List<CompletableFuture<ResponseEntity<FilmDto>>> futuresList = new ArrayList<>();

        for(URI uri: uris) {
            futuresList.add(getObjectByURI(uri, FilmDto.class));
        }
        // wait for all completed
        futuresList.stream().map(CompletableFuture::join);

        return getAllData(futuresList);
    }

    private FilmDto getFromComletableFuture(CompletableFuture<ResponseEntity<FilmDto>> cf) {
        HttpStatus statusCode = HttpStatus.NOT_FOUND;
        FilmDto dto = null;
        try {
            statusCode = cf.get().getStatusCode();
            dto = cf.get().getBody();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (statusCode == HttpStatus.OK) ? dto : null;
    }

    /**
     * Make get request to endpoint with object details
     * @param endpoint full uri to resource
     * @param type of object
     * @param <T>
     * @return object details mapped to given type
     */
    private  <T> CompletableFuture<ResponseEntity<T>> getObjectByURI(URI endpoint, Class<T> type) {
        return apiConsumer.getResponseAsync(endpoint.toString(), type);
    }

    /**
     * Make get request to endpoint with search query based on phrase to fetch data from all pages
     * @param endpoint full url
     * @param phrase to build parameter query
     * @param type of object to map (target type)
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
     * Map list to object of given type
     * @param fromList list of LinkedHashMap<String, String> to process
     * @param type of object to map (target type)
     * @param <T>
     * @return mapped list
     */
    private <T> List<T> mapToListOfObjectType(List<Object> fromList, Class<T> type) {
        return fromList.stream()
                .map(ele -> objectMapper.convertValue(ele, type))
                .collect(Collectors.toList());
    }

    private Map<URI, String> getAllData(List<CompletableFuture<ResponseEntity<FilmDto>>> futuresList) {
        Map<URI, String> map = new HashMap<>();
        for(CompletableFuture<ResponseEntity<FilmDto>> cf : futuresList) {
            map.put(getKey(cf), getValue(cf));
        }
        return map;
    }

    private URI getKey(CompletableFuture<ResponseEntity<FilmDto>> cf) {
        return Objects.requireNonNull(getFromComletableFuture(cf)).getUrl();
    }

    private String getValue(CompletableFuture<ResponseEntity<FilmDto>> cf) {
        return Objects.requireNonNull(getFromComletableFuture(cf)).getTitle();
    }

    private String buildQueryUrl(String endpoint) {
        return UriComponentsBuilder.fromUriString(endpoint).toUriString();
    }

    private String buildQueryUrl(String endpoint, String phrase) {
        return UriComponentsBuilder.fromUriString(endpoint).queryParam("search", phrase).toUriString();
    }
}
