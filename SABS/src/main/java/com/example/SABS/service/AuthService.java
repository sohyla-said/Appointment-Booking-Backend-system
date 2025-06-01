package com.example.SABS.service;

import com.example.SABS.DTO.AuthenticationRequest;
import com.example.SABS.config.ApplicationConfig;
import com.example.SABS.config.JwtService;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.User;
import com.example.SABS.repository.ServiceProviderRepository;
import com.example.SABS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;
    private ApplicationConfig applicationConfig;

    public ResponseEntity<?> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email " + user.getEmail() + " is already registered.");
        }
         if (user.getFname() == null || user.getLname() == null ||
                 user.getEmail() == null || user.getPassword() == null ||
                 user.getPhone() == null || user.getRole() == null){
             throw new IllegalArgumentException("all fields must be filled.");
         }
        User newUser = new User();
        newUser.setFname(user.getFname());
        newUser.setLname(user.getLname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhone(user.getPhone());
        newUser.setRole(user.getRole());
        userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(newUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("user", newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    public ResponseEntity<?> login(AuthenticationRequest authenticationRequest) {
        User foundUser = userRepository.findByEmail(authenticationRequest.getEmail()).orElse(null);
        if (foundUser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), foundUser.getPassword())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String jwtToken = jwtService.generateToken(foundUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("user", foundUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> registerServiceProvider(ServiceProvider serviceProvider) {
        if (userRepository.findByEmail(serviceProvider.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Service provider with email " + serviceProvider.getEmail() + " is already registered.");
        }
        if (serviceProvider.getFname() == null || serviceProvider.getLname() == null ||
                serviceProvider.getEmail() == null || serviceProvider.getPassword() == null ||
                serviceProvider.getPhone() == null || serviceProvider.getRole() == null ||
                serviceProvider.getSpeciality() == null){
            throw new IllegalArgumentException("all fields must be filled.");
        }
        if (serviceProvider.getRole().toString() != "SERVICE_PROVIDER"){
            throw new IllegalArgumentException("role must be SERVICE_PROVIDER");
        }
        ServiceProvider newServiceProvider = new ServiceProvider();
        newServiceProvider.setFname(serviceProvider.getFname());
        newServiceProvider.setLname(serviceProvider.getLname());
        newServiceProvider.setEmail(serviceProvider.getEmail());
        newServiceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));
        newServiceProvider.setPhone(serviceProvider.getPhone());
        newServiceProvider.setRole(serviceProvider.getRole());
        newServiceProvider.setSpeciality(serviceProvider.getSpeciality());
        userRepository.save(newServiceProvider);
        serviceProviderRepository.save(newServiceProvider);
        String jwtToken = jwtService.generateToken(newServiceProvider);
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("service_provider", newServiceProvider);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
