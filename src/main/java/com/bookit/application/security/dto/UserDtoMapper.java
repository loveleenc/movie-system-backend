package com.bookit.application.security.dto;

import com.bookit.application.security.entity.User;
import com.bookit.application.security.entity.types.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {

    public User toUser(UserDto userDto){
        return new User(userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEmail(),
                UserDtoMapper.getRoles(userDto.getRoles())
        );
    }

    public static UserInfoDto getRoleDto(List<Role> roles, String name){
        return new UserInfoDto(roles.stream().map(Role::code).toList(), name);
    }

    private static List<Role> getRoles(List<String> roles) throws IllegalArgumentException{
        return roles.stream().map(role -> {
            Role roleEnum = Role.getRoleEnum(role);
            if(roleEnum == null){
                throw new IllegalArgumentException(String.format("Provided role %s is incorrect.", role));
            }
            return roleEnum;
        }).toList();
    }

    public UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map(Role::code).toList());
        return userDto;
    }
}
