// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.g4share.pipelineDemo.app.domain.User;
import com.g4share.pipelineDemo.app.error.UserServiceError;
import com.g4share.pipelineDemo.app.helpers.ApiHelper;
import com.g4share.pipelineDemo.app.helpers.RestResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.api.url}")
    private String userApiUrl;

    private final ApiHelper apiHelper ;

    @Override
    public List<User> getAll() {
        return apiHelper.getAll(userApiUrl);
    }

    @Override
    public RestResult<User, UserServiceError> addUser(User user) throws JsonProcessingException {
        return apiHelper.post(userApiUrl, user, User.class, UserServiceError.class);
    }

    @Override
    public void delete(Long userId) {
        apiHelper.delete(userApiUrl, userId);
    }
}
