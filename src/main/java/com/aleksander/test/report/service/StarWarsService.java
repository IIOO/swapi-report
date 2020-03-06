package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.FilmResponseDto;
import com.aleksander.test.report.dto.response.PersonResponseDto;
import com.aleksander.test.report.dto.response.PlanetResponseDto;
import com.aleksander.test.report.dto.response.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StarWarsService {
    public final static String PLANETS_URL = "https://swapi.co/api/planets/";
    public final static String PEOPLE_URL = "https://swapi.co/api/people/";

    private final ApiConsumer apiConsumer;

    private final ObjectMapper objectMapper;


    public List<PlanetResponseDto> getPlanetsByPhrase(String phrase) {
        return getByPhrase(PLANETS_URL, phrase, PlanetResponseDto.class);
    }

    public List<PersonResponseDto> getPeopleByPhrase(String phrase) {
        return getByPhrase(PEOPLE_URL, phrase, PersonResponseDto.class);
    }

    /**
     * Get film names for given set (async)
     * @param uris to fetch details
     * @return map with film uri and it's name
     */
    public Map<URI, String> getFilmsByUris(Set<URI> uris) {
        List<CompletableFuture<ResponseEntity<FilmResponseDto>>> futuresList = new ArrayList<>();

        for(URI uri: uris) {
            futuresList.add(getObjectByURI(uri, FilmResponseDto.class));
        }
        // wait for all completed and map results
        return futuresList.stream()
                .map(CompletableFuture::join)
                .map(cf -> Objects.requireNonNull(cf.getBody()))
                .collect(Collectors.toMap(FilmResponseDto::getUrl, FilmResponseDto::getTitle));
    }

    /**
     * Make get request to endpoint containing object details
     * @param endpoint full uri to resource (eg. https://swapi.co/api/films/1)
     * @param type of object
     * @param <T>
     * @return object details mapped to given type
     */
    private  <T> CompletableFuture<ResponseEntity<T>> getObjectByURI(URI endpoint, Class<T> type) {
        return apiConsumer.getResponseAsync(endpoint.toString(), type);
    }

    /**
     * Make get request to endpoint with search query based on phrase to fetch data from all pages
     * @param endpoint full url (eg. https://swapi.co/api/planets/)
     * @param phrase to build parameter query
     * @param type of object to map (target type)
     * @param <T>
     * @return list of all objects matching given query phrase
     */
    private <T> List<T> getByPhrase(String endpoint, String phrase, Class<T> type) {
        String url = buildQueryUrl(endpoint, phrase);

        QueryResponse response = apiConsumer.getResponse(url, QueryResponse.class);

        // add all first page results
        List<T> list = new ArrayList<>(mapToListOfObjectType(response.getResults(), type));

        if (Objects.nonNull(response.getNext())) {
             List<CompletableFuture<ResponseEntity<QueryResponse>>> futuresList = new ArrayList<>();

            for(URI uri: getNextPageUris(response, endpoint, phrase)) {
                futuresList.add(getObjectByURI(uri, QueryResponse.class));
            }

            // wait for all completed and map results
            List<T> otherPagesResults = futuresList.stream()
                    .map(CompletableFuture::join)
                    .map(cf -> Objects.requireNonNull(cf.getBody()).getResults())
                    .map(res -> mapToListOfObjectType(res, type))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            // add all next pages results
            list.addAll(otherPagesResults);
        }
        return list;
    }

    /**
     * Map list to objects of given type
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

    private String buildQueryUrl(String endpoint, String phrase) {
        return UriComponentsBuilder.fromUriString(endpoint).queryParam("search", phrase).toUriString();
    }


    private List<URI> getNextPageUris(QueryResponse response, String endpoint, String phrase) {
        int pages = calculatePageCount(response.getResults().size(), response.getCount());
        List<URI> nextPagesUris = new ArrayList<>();

        // start from second page because first page already requested
        for (int i = 2; i <= pages; i++) {
            nextPagesUris.add(buildQueryUrl(endpoint, i, phrase));
        }
        return nextPagesUris;
    }

    private URI buildQueryUrl(String endpoint, int page,  String phrase) {
        return URI.create(endpoint + "?page=" + page + "&search=" + phrase);
    }

    private int calculatePageCount(int pageSize, int count) {
        // round up
        return (count + (pageSize - 1)) / pageSize;
    }
}
