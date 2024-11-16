package com.joe.u_learning.mapper;

import com.joe.u_learning.dto.RegisterDto;
import com.joe.u_learning.dto.UserDto;
import com.joe.u_learning.entity.User;


public class UserMapper {

    public static User toEntity(RegisterDto registerDto){
        return User.builder()
                .username(registerDto.username())
                .password(registerDto.password())
                .email(registerDto.email())
                .phoneNumber(registerDto.phoneNumber())
                .address(registerDto.address())
                .build();
    }

    public static  UserDto toDto(User user){
        return new UserDto(user.getId(),user.getUsername(),user.getEmail(),user.getAddress(),user.getPhoneNumber());
    }
}
