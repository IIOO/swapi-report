package com.aleksander.test.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiConsumer {
    private final RestTemplate restTemplate;

    protected <T> T getResponse(String url, Class<T> type) {
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, getHttpEntity(), type);
        HttpStatus statusCode = response.getStatusCode();
//      Handle other http status?
        return (statusCode == HttpStatus.OK) ? response.getBody() : null;
    }

    @Async("asyncExecutor")
    protected <T> CompletableFuture<ResponseEntity<T>> getResponseAsync(String url, Class<T> type) {
        log.debug("Request: " + url);
        return CompletableFuture.completedFuture(restTemplate.exchange(url, HttpMethod.GET, getHttpEntity(), type));
    }


    /**
     * Add user agent to mock browser and system data, required to build and test application through maven. Otherwise SWAPI return 403.
     * https://stackoverflow.com/questions/46440982/consuming-rest-works-with-curl-but-does-not-work-when-i-am-usiing-resttemplate
     * @return
     */
    private HttpEntity<Object> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:55.0) Gecko/20100101 Firefox/55.0");
        return new HttpEntity<>(headers);
    }
}
