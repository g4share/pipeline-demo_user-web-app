// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
}
