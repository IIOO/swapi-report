package com.aleksander.test.report.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        HttpStatus.Series series = httpResponse.getStatusCode().series();
        return (series == CLIENT_ERROR || series == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        HttpStatus.Series series = httpResponse.getStatusCode().series();

        if (series == SERVER_ERROR) {
            log.error("SERVER_ERROR");
            throw new ExternalApiException("External API error, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (series == CLIENT_ERROR) {
            log.error("CLIENT_ERROR");
            throw new ExternalApiException("Error caused by external API misuse. Please contact application author.", HttpStatus.NOT_FOUND);
        }
    }
}

