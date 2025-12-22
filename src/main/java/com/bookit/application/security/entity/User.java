package com.bookit.application.security.entity;


import com.bookit.application.types.AccountStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.bookit.application.types.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private List<Role> roles;
    private AccountStatus accountStatus;
    private Long id;

    public User(@NonNull String firstName,
                @NonNull String lastName,
                @NonNull String username,
                @NonNull String password,
                @NonNull String email,
                @NonNull List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.code()));
        }
        return authorities;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setRoles(@NonNull List<Role> roles) {
        this.roles = roles;
    }

    public void setAccountStatus(@NonNull AccountStatus status) throws IllegalArgumentException{
        this.accountStatus = status;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    @Override
    public boolean isEnabled(){
        return this.accountStatus.equals(AccountStatus.ACTIVE);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  public void setUsername(@NonNull String username) {
    this.username = username;
  }
}

