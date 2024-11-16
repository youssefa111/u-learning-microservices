package com.joe.u_learning.controller;

import com.joe.u_learning.dto.LoginDto;
import com.joe.u_learning.dto.RegisterDto;
import com.joe.u_learning.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){

        return ResponseEntity.created(URI.create("/register")).body(authenticationService.register(registerDto));
    }
    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authenticationService.login(loginDto));
    }
}
