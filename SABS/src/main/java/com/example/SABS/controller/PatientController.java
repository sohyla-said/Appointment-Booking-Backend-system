package com.example.SABS.controller;

import com.example.SABS.DTO.UpdateProfileRequest;
import com.example.SABS.model.Appointment;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.Slots;
import com.example.SABS.model.User;
import com.example.SABS.service.AppointmentSchedulingService;
import com.example.SABS.service.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private AppointmentSchedulingService appointmentSchedulingService;


    //Principal is an interface in Spring Security that
    // represents the currently authenticated user. It helps in identifying the logged-in user who made the request.
//    When a user logs in and gets authenticated, Spring Security creates a SecurityContext that stores the authentication details.
//    The Principal object provides access to the currently authenticated user’s identity.
//    It is typically an instance of UserDetails.

    @GetMapping("/viewprofile")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<User> viewPatientProfile(@RequestParam String email, Principal principal) {
        String tokenEmail = principal.getName(); // Extract email from the authenticated token
        if (!tokenEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(userManagementService.viewPatientProfile(email));
    }

    @PostMapping("/editprofile")
    @RolesAllowed({"PATIENT"})
    //use principle to get the email of the logged in user instead of passing it as a parameter
    public ResponseEntity<User> updatePatientProfile(@RequestBody UpdateProfileRequest updateProfileRequest, Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(userManagementService.updatePatientProfile(email, updateProfileRequest));
    }

    @GetMapping("/browseAvailableSlots")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<List<Slots>> browseAvailableSlots(Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(appointmentSchedulingService.getAvailableSlots());
    }

    @GetMapping("/browseAvailableProviders")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<List<ServiceProvider>> browseAvailableServiceProviders(Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(appointmentSchedulingService.getAvailableServiceProviders());
    }

    @PostMapping("/reserveSlot")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<String> reserveSlot(@RequestBody Integer slotId, Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(appointmentSchedulingService.reserveSlot(slotId));
    }

    @PostMapping("/confirmAppointment")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<String> confirmAppointment(@RequestBody Integer slotId, Principal principal){
        String email = principal.getName();
        return ResponseEntity.ok(appointmentSchedulingService.confirmAppointment(slotId, email));
    }

    @GetMapping("/viewAppointmentHistory")
    @RolesAllowed({"PATIENT"})
    public ResponseEntity<List<Appointment>> viewAppointmentHistory(Principal principal){
        String email = principal.getName();
        if (!email.equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ❌ Prevents accessing other users' profiles
        }
        return ResponseEntity.ok(userManagementService.viewPatientAppointmentHistory(email));
    }
}
