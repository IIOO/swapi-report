package com.aleksander.test.report.service;

import com.aleksander.test.report.dto.response.PlanetResponseDto;
import com.aleksander.test.report.dto.response.QueryResponse;
import com.aleksander.test.report.exception.ExternalApiException;
import com.aleksander.test.report.exception.RestTemplateResponseErrorHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        apiConsumer = new ApiConsumer(restTemplate);
    }

    @Test
    void getResponseWithNextPage() {
        // when
        QueryResponse response = apiConsumer.getResponse(SEARCH_PLANETS_URL, QueryResponse.class);
        String query = URI.create(response.getNext()).getQuery();

        // then
        assertTrue(response.getResults().size() > 0);
        assertTrue(query.contains("page=2"));
    }

    @Test
    void invalidUrl() {
        // when
        ExternalApiException exception = assertThrows(ExternalApiException.class, () -> {
            apiConsumer.getResponse(INVALID_URL, QueryResponse.class);
        });

        // then
        assertEquals(404, exception.getStatus().value());
    }

    @Test
    void getAsyncResponse() throws ExecutionException, InterruptedException {
        // when
        CompletableFuture<ResponseEntity<PlanetResponseDto>> cf = apiConsumer.getResponseAsync(PLANET_URL, PlanetResponseDto.class);

        // then
        assertEquals(PLANET_URL, cf.get().getBody().getUrl().toString());
    }
}
