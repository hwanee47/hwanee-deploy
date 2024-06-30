package com.deploy.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUser {

    private String username;
    private String email;

    public ResUser(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
