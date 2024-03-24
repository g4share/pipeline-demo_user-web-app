// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiHelper {

    private final RestTemplate restTemplate;
    private final ObjectMapperHelper objectMapperHelper;

    public <T> List<T> getAll(String apiUrl) {
        ResponseEntity<List<T>> rateResponse =
                restTemplate.exchange(apiUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() { });
        return rateResponse.getBody();
    }

    public void delete(String apiUrl, Long entityId) {
        restTemplate.exchange(
                apiUrl + "/" + entityId,
                HttpMethod.DELETE,
                null,
                Void.class);
    }

    public <T, R, F> RestResult<R, F> post(String apiUrl, T entity,
                                           Class<R> resultClass, Class<F> failureClass) throws JsonProcessingException {
        try {
            ResponseEntity<R> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    jsonEntity(entity),
                    resultClass);

            return new RestResult.Success<>(response.getBody());
        } catch (HttpClientErrorException ex) {
            F error = objectMapperHelper.toFailure(ex.getResponseBodyAsString(), failureClass);
            return new RestResult.Failure<>(error);
        }
    }

    private <T> HttpEntity<T> jsonEntity(T entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(entity, headers);
    }
}
