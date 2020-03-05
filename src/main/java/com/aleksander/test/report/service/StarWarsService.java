package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.FilmResponseDto;
import com.aleksander.test.report.dto.response.PersonResponseDto;
import com.aleksander.test.report.dto.response.PlanetResponseDto;
import com.aleksander.test.report.dto.response.QueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
        // wait for all completed
        futuresList.stream().map(CompletableFuture::join);

        return getAllFilmData(futuresList);
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
        List<T> list = new ArrayList<>();
        String url = buildQueryUrl(endpoint, phrase);

        QueryResponse response = apiConsumer.getResponse(url, QueryResponse.class);

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
            // add all first page results
            list.addAll(mapToListOfObjectType(response.getResults(), type));
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

    private <T> T getDataFromComletableFuture(CompletableFuture<ResponseEntity<T>> cf) {
        HttpStatus statusCode = HttpStatus.NOT_FOUND;
        T dto = null;
        try {
            statusCode = cf.get().getStatusCode();
            dto = cf.get().getBody();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (statusCode == HttpStatus.OK) ? dto : null;
    }

    private Map<URI, String> getAllFilmData(List<CompletableFuture<ResponseEntity<FilmResponseDto>>> futuresList) {
        Map<URI, String> map = new HashMap<>();
        for(CompletableFuture<ResponseEntity<FilmResponseDto>> cf : futuresList) {
            map.put(getKey(cf), getValue(cf));
        }
        return map;
    }

    private URI getKey(CompletableFuture<ResponseEntity<FilmResponseDto>> cf) {
        return Objects.requireNonNull(getDataFromComletableFuture(cf)).getUrl();
    }

    private String getValue(CompletableFuture<ResponseEntity<FilmResponseDto>> cf) {
        return Objects.requireNonNull(getDataFromComletableFuture(cf)).getTitle();
    }


    private String buildQueryUrl(String endpoint, String phrase) {
        return UriComponentsBuilder.fromUriString(endpoint).queryParam("search", phrase).toUriString();
    }

    private URI buildQueryUrl(String endpoint, int page,  String phrase) {
        return URI.create(endpoint + "?page=" + page + "&search=" + phrase);
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

    private int calculatePageCount(int pageSize, int count) {
        // round up
        return (count + (pageSize - 1)) / pageSize;
    }
}
