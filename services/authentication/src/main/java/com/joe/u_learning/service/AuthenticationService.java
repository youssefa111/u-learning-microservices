package com.joe.u_learning.service;

import com.joe.u_learning.dto.LoginDto;
import com.joe.u_learning.dto.RegisterDto;
import com.joe.u_learning.dto.UserDto;

public interface AuthenticationService {

     String register(RegisterDto registerDto);
     UserDto login(LoginDto loginDto);

}
