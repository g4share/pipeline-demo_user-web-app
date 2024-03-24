// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectMapperHelper {

    private final ObjectMapper objectMapper;

    public <F> F toFailure(String failure, Class<F> clazz) throws JsonProcessingException {
        return objectMapper.readValue(failure, clazz);
    }
}
