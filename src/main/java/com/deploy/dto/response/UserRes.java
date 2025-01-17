package com.deploy.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRes {

    private String username;
    private String email;

    public UserRes(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
