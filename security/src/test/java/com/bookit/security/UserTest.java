package com.bookit.security;

import com.bookit.security.email.EmailService;
import com.bookit.security.entity.User;
import com.bookit.security.entity.types.Role;
import com.bookit.security.user.UserAccountCreationHelper;
import com.bookit.security.user.UserService;
import com.bookit.security.user.token.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.*;

public class UserTest {
    private static UserService userService;
    private static CustomUserDetailsService customUserDetailsService;
    private static TokenService tokenService;
    private static EmailService emailService;
    private static UserAccountCreationHelper userAccountCreationHelper;
    private User user;


    @BeforeAll
    public static void beforeAll() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        tokenService = mock(TokenService.class);
        emailService = mock(EmailService.class);
        userAccountCreationHelper = mock(UserAccountCreationHelper.class);
        userService = new UserService(customUserDetailsService,
                tokenService, emailService, userAccountCreationHelper);
    }

    @BeforeEach
    public void before() {
        this.user = new User("Bob",
                "Marley", "bobbyTable1", "hyh9ssH8*", "marleybob@mail.com",
                List.of(Role.REGULAR_USER));
    }

    @Test
    public void test_userCreationFailsWhenUsernameLengthLessThan8AndMoreThan15_OrHasSpecialCharacters() {
        this.user.setUsername("abC21aa");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when username is less than 8 characters");

        this.user.setUsername("userj7h5dG5jaqw");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user));

        this.user.setUsername("userj7hsi@");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user));
    }

    @Test
    public void test_userCreationFailsWhenPasswordDoesNotMatchRequiredCriteria() {
        this.user.setPassword("*aA124s");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password is less than 8 characters");

        this.user.setPassword("*aA124aqtd8d&nGTkaq1");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password is more than 20 characters");

        this.user.setPassword("HY&*gtauho");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password does not contain a digit");

        this.user.setPassword("HY&*3QWEQ3");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password does not contain an lowercase character");

        this.user.setPassword("htft**8@1qa");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password does not contain an uppercase character");

        this.user.setPassword("Hyujgfrhj9");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when password does not contain a special character");
    }

    @Test
    public void test_userCreationFailsWhenEmailDoesNotMatchRegex() {
        this.user.setEmail("blablablah@nomail");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when email does not match the expected format");


    }


    //if no role is specified
    // if role is admin
    // if role is theatreowner - account creation email is sent
    // if role is regular user - account activation email is sent
    // if role is regular user - cart is created
    //

    @Test
    public void test_userCreationFailsWhenInvalidRolesAreProvided() {
        this.user.setRoles(List.of());
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when no roles are specified for the user");

        this.user.setRoles(List.of(Role.ADMIN));
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(this.user),
                "User creation fails when ADMIN role is specified for the user");
    }

    @Test
    public void test_verifyCreationOfValidUserAsTheatreOperator() {
        this.user.setRoles(List.of(Role.THEATRE_OWNER));
        User createdUser = new User(
                this.user.getFirstName(),
                this.user.getLastName(),
                this.user.getUsername(),
                this.user.getPassword(),
                this.user.getEmail(),
                this.user.getRoles()
        );
        createdUser.setId(1L);
        when(customUserDetailsService.createUser(this.user)).thenReturn(createdUser);
        when(userAccountCreationHelper.usernameCriteriaFulfilled(user.getUsername())).thenReturn(true);
        when(userAccountCreationHelper.passwordCriteriaFulfilled(user.getPassword())).thenReturn(true);
        when(userAccountCreationHelper.emailIsValid(user.getEmail())).thenReturn(true);
        String randomMessage = "blah blah blah";
        when(userAccountCreationHelper.createAccountActivationEmailMessage(user.getUsername())).thenReturn(randomMessage);

        userService.createUser(this.user);
        verify(userAccountCreationHelper, times(1)).createAccountActivationEmailMessage(user.getUsername());
        verify(emailService, times(1)).sendEmail(user.getEmail(),
                UserAccountCreationHelper.accountActivationEmailSubject, randomMessage);

    }


    @Test
    public void test_verifyCreationOfValidUserAsCustomer() {
        this.user.setRoles(List.of(Role.REGULAR_USER));
        User createdUser = new User(
                this.user.getFirstName(),
                this.user.getLastName(),
                this.user.getUsername(),
                this.user.getPassword(),
                this.user.getEmail(),
                this.user.getRoles()
        );
        createdUser.setId(1L);
        String randomMessage2 = "more blah blah blah welcoming crap";
        ReflectionTestUtils.setField(userService, "clientUrl", "https://example.com/");

        when(customUserDetailsService.createUser(this.user)).thenReturn(createdUser);
        when(userAccountCreationHelper.usernameCriteriaFulfilled(user.getUsername())).thenReturn(true);
        when(userAccountCreationHelper.passwordCriteriaFulfilled(user.getPassword())).thenReturn(true);
        when(userAccountCreationHelper.emailIsValid(user.getEmail())).thenReturn(true);
        when(userAccountCreationHelper.
                createAccountActivationEmailMessage(any(String.class), any(UrlResource.class)))
                .thenReturn(randomMessage2);
        userService.createUser(this.user);
        verify(emailService, times(1)).sendEmail(user.getEmail(),
                UserAccountCreationHelper.accountActivationEmailSubject, randomMessage2);
    }
}
