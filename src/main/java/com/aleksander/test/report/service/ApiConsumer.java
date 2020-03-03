package com.aleksander.test.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ApiConsumer {
    private final RestTemplate restTemplate;

    protected <T> T getResponse(String url,Class<T> type) {
        ResponseEntity<T> response = restTemplate.getForEntity(url, type);
        HttpStatus statusCode = response.getStatusCode();
//      Handle other http status?
        return (statusCode == HttpStatus.OK) ? response.getBody() : null;
    }

    @Async("asyncExecutor")
    protected <T> CompletableFuture<ResponseEntity<T>> getResponseAsync(String url, Class<T> type) {
        return CompletableFuture.completedFuture(restTemplate.getForEntity(url, type));
    }
}
