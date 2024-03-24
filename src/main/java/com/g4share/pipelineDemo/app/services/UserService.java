// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.g4share.pipelineDemo.app.domain.User;
import com.g4share.pipelineDemo.app.error.UserServiceError;
import com.g4share.pipelineDemo.app.helpers.RestResult;

import java.util.List;

public interface UserService {

    List<User> getAll();

    void delete(Long id);

    RestResult<User, UserServiceError> addUser(User user) throws JsonProcessingException;
}
