package com.aleksander.test.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
}
