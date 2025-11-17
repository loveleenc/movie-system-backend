package com.bookit.application.controller.user;


import com.bookit.application.dto.user.LoginDto;
import com.bookit.application.dto.user.UserDto;
import com.bookit.application.dto.user.UserDtoMapper;
import com.bookit.application.security.entity.User;
import com.bookit.application.services.CartService;
import com.bookit.application.services.UserService;
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
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    private UserDtoMapper userDtoMapper;
    private CartService cartService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UserController(UserService userService, UserDtoMapper userDtoMapper, CartService cartService, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.cartService = cartService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        User user = this.userDtoMapper.toUser(userDto);
        User createdUser = this.userService.createUser(user);
        this.cartService.createCartForNewUser(createdUser.getId());
        return new ResponseEntity<>(this.userDtoMapper.toUserDto(createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword());
        try{
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            System.out.println("is user authenticated: " + authenticationResponse.isAuthenticated());
            SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
            context.setAuthentication(authenticationResponse);
            SecurityContextHolder.getContextHolderStrategy().setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return new ResponseEntity<>("Login was successful", HttpStatus.OK);
        }
        catch(BadCredentialsException e){
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/loginstatus")
    public ResponseEntity<String> getLoginStatus() {
        return new ResponseEntity<>("User is logged in.", HttpStatus.OK);
    }
}
