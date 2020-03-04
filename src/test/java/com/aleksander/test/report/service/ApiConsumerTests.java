package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.PlanetResponseDto;
import com.aleksander.test.report.dto.response.QueryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


public class ApiConsumerTests {
    private final static String SEARCH_PLANETS_URL = "https://swapi.co/api/planets/?search=a";
    private final static String PLANET_URL = "https://swapi.co/api/planets/1/";
    private final static String INVALID_URL = "https://swapi.co/api/plane/";

    private static ApiConsumer apiConsumer;

    @BeforeAll
    static void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        apiConsumer = new ApiConsumer(restTemplate);
    }

    @Test
    void getResponse() {
        QueryResponse response = apiConsumer.getResponse(SEARCH_PLANETS_URL, QueryResponse.class);

        assertEquals("https://swapi.co/api/planets/?page=2&search=a", response.getNext());
        assertTrue(response.getResults().size() > 0);
    }

    @Test
    void invalidUrl() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            apiConsumer.getResponse(INVALID_URL, QueryResponse.class);
        });

        assertEquals(404, exception.getRawStatusCode());
    }

    @Test
    void getAsyncResponse() throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<PlanetResponseDto>> cf = apiConsumer.getResponseAsync(PLANET_URL, PlanetResponseDto.class);

        assertEquals(PLANET_URL, cf.get().getBody().getUrl().toString());
    }
}
