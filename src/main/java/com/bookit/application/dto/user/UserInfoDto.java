package com.bookit.application.dto.user;

import java.util.List;

public class UserInfoDto {
    private List<String> roles;
    private String name;

    public UserInfoDto(List<String> roles, String name) {
        this.roles = roles;
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }
}
