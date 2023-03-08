package com.data;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class User {

    @NotEmpty(message = "{msg.username.notEmpty}")
    private String name;

    private String passwd;

}
