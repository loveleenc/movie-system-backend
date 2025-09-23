package com.bookit.application.controller;


import com.bookit.application.dto.user.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping
    String registerUserAccount(UserDto userDto){
        return "";
    }


}
