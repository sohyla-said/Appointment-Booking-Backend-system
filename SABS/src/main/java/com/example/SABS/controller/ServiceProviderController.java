package com.example.SABS.controller;

import com.example.SABS.DTO.UpdateProfileRequest;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.service.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/serviceprovider")
public class ServiceProviderController {

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/editSPProfile")
    @RolesAllowed({"SERVICE_PROVIDER"})
    public ResponseEntity<ServiceProvider> editServiceProviderProfile(@RequestBody UpdateProfileRequest updateProfileRequest, Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userManagementService.editServiceProviderProfile(email, updateProfileRequest));
    }

    @GetMapping("/viewSPProfile")
    @RolesAllowed({"SERVICE_PROVIDER"})
    public ResponseEntity<ServiceProvider> viewServiceProviderProfile(@RequestParam String email, Principal principal){
        String tokenEmail = principal.getName();
        if (!email.equals(tokenEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userManagementService.viewServiceProviderProfile(email));
    }

    @PostMapping("setSlot")
    public ResponseEntity<?> setSPAvailableWorkingHours(@RequestBody String dateTimeStr, Principal principal){
        String tokenEmail = principal.getName();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr.replace("\"", ""));
        return ResponseEntity.ok(userManagementService.setSlot(tokenEmail, dateTime));
    }
}
