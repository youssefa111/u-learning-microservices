package com.joe.u_learning.dto;

public record UserDto(
        int id,
        String username,
        String email,
        String address,
        String phoneNumber
) {
}
