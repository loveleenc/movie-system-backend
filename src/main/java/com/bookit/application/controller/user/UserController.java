package com.bookit.application.controller.user;


import com.bookit.application.dto.user.LoginDto;
import com.bookit.application.dto.user.UserInfoDto;
import com.bookit.application.dto.user.UserDto;
import com.bookit.application.dto.user.UserDtoMapper;
import com.bookit.application.security.entity.User;
import com.bookit.application.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    private UserDtoMapper userDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UserController(UserService userService, UserDtoMapper userDtoMapper, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) throws MalformedURLException {
        User user = this.userDtoMapper.toUser(userDto);
        User createdUser = this.userService.createUser(user);
        return new ResponseEntity<>(this.userDtoMapper.toUserDto(createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword());
        try{
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
            context.setAuthentication(authenticationResponse);
            SecurityContextHolder.getContextHolderStrategy().setContext(context);
            securityContextRepository.saveContext(context, request, response);
            User user = (User) authenticationResponse.getPrincipal();
            UserInfoDto userInfoDto = UserDtoMapper.getRoleDto(user.getRoles(), user.getFirstName());
            return new ResponseEntity<>(userInfoDto, HttpStatus.OK);
        }
        catch(BadCredentialsException e){
            UserInfoDto userInfoDto = new UserInfoDto(List.of(), "");
            return new ResponseEntity<>(userInfoDto, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/loginstatus")
    public ResponseEntity<String> getLoginStatus() {
        return new ResponseEntity<>("User is logged in", HttpStatus.OK);
    }


    @PatchMapping("/user/activate/{id}")
    public ResponseEntity<String> activateAccount(@PathVariable String id){
        this.userService.activateUserAccount(id);
        return new ResponseEntity<>("User Account has been activated successfully!", HttpStatus.OK);
    }
}
