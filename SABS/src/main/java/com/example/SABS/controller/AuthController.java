package com.example.SABS.controller;

import com.example.SABS.DTO.AuthenticationRequest;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.User;
import com.example.SABS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private  AuthService authService;


    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody User user){
//        return ResponseEntity.ok(authService.register(user));
        return authService.registerUser(user);
    }


    @PostMapping("/registerServiceProvider")
    public ResponseEntity<?> registerServiceProvider(@RequestBody ServiceProvider serviceProvider){
//        return ResponseEntity.ok(authService.register(user));
        return authService.registerServiceProvider(serviceProvider);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        return authService.login(authenticationRequest);
    }
}
