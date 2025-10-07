package com.bookit.application.controller.user;


import com.bookit.application.dto.user.UserDto;
import com.bookit.application.dto.user.UserDtoMapper;
import com.bookit.application.security.entity.User;
import com.bookit.application.services.CartService;
import com.bookit.application.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private UserDtoMapper userDtoMapper;
    private CartService cartService;

    public UserController(UserService userService, UserDtoMapper userDtoMapper, CartService cartService) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.cartService = cartService;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto){
        User user = this.userDtoMapper.toUser(userDto);
        User createdUser = this.userService.createUser(user);
        this.cartService.createCartForNewUser(createdUser.getId());
        return new ResponseEntity<>(this.userDtoMapper.toUserDto(createdUser), HttpStatus.CREATED);
    }


}
