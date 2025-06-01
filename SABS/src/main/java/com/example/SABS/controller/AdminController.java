package com.example.SABS.controller;

import com.example.SABS.DTO.SlotAvailabilityRequest;
import com.example.SABS.service.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/activateAccount")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> activateAccount(@RequestParam String email, Principal principal){
        return ResponseEntity.ok(userManagementService.activateAccount(email));
    }
    @GetMapping("/deactivateAccount")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deactivateAccount(@RequestParam String email, Principal principal){

        return ResponseEntity.ok(userManagementService.deactivateAccount(email));
    }
    @PostMapping("/resetPassword")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> resetPassword(@RequestParam String email,@RequestBody String password, Principal principal){

        return ResponseEntity.ok(userManagementService.resetPassword(email, password));
    }

    @GetMapping("/changeSPSpeciality")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> changeServiceProviderSpeciality(@RequestParam String email, @RequestParam String newSpeciality, Principal principal){

        return ResponseEntity.ok(userManagementService.changeServiceProviderSpeciality(email, newSpeciality));
    }

    @PostMapping("/setSlotAvailability")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> setSlotAvailability(@RequestBody SlotAvailabilityRequest request){
        System.out.println(request.getAvailable());
        return ResponseEntity.ok(userManagementService.setSlotAvailability(request.getSlotId(), request.getAvailable()));
    }

}
