package com.joe.u_learning.service;

import com.joe.u_learning.dto.LoginDto;
import com.joe.u_learning.dto.RegisterDto;
import com.joe.u_learning.dto.UserDto;
import com.joe.u_learning.entity.User;
import com.joe.u_learning.mapper.UserMapper;
import com.joe.u_learning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;

    @Transactional
    @Override
    public String register(RegisterDto registerDto) {
        User user = UserMapper.toEntity(registerDto);
        userRepository.save(user);
        return "The Registration completed successfully!!";
    }

    @Override
    public UserDto login(LoginDto loginDto) {
        var result = userRepository.findByUsername(loginDto.username()).orElseThrow(()-> new RuntimeException("Wrong username or password"));
        return UserMapper.toDto(result);
    }
}
