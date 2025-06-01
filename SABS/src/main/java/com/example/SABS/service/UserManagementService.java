package com.example.SABS.service;

import com.example.SABS.DTO.UpdateProfileRequest;
import com.example.SABS.model.Appointment;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.Slots;
import com.example.SABS.model.User;
import com.example.SABS.repository.AppointmentRepository;
import com.example.SABS.repository.ServiceProviderRepository;
import com.example.SABS.repository.SlotRepository;
import com.example.SABS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    public User viewPatientProfile(String email) {
        User foundUser = userRepository.findByEmail(email).orElse(null);

        if (foundUser == null){
            throw new NoSuchElementException("User with email " + email + "is not found.");
        }
        else{
            return foundUser;
        }
    }

    public User updatePatientProfile(String email, UpdateProfileRequest updateProfileRequest) {
        User foundUser = userRepository.findByEmail(email).orElse(null);
        if (foundUser == null){
            throw new NoSuchElementException("User with email " + email + " is not found.");
        }
        else{
            if (updateProfileRequest.getFname() != null){
                foundUser.setFname(updateProfileRequest.getFname());
            }
            if (updateProfileRequest.getLname() != null){
                foundUser.setLname(updateProfileRequest.getLname());
            }
            if (updateProfileRequest.getPassword() != null){
                foundUser.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));;
            }
            if (updateProfileRequest.getPhone() != null){
                foundUser.setPhone(updateProfileRequest.getPhone());
            }
            userRepository.save(foundUser);
            return foundUser;
        }
    }

    public ServiceProvider editServiceProviderProfile(String email, UpdateProfileRequest updateProfileRequest) {
        ServiceProvider foundSP = serviceProviderRepository.findByEmail(email).orElse(null);
        if (foundSP == null){
            throw new NoSuchElementException("Service provider with email " + email + " is not found.");
        }
        else{
            if (updateProfileRequest.getFname() != null){
                foundSP.setFname(updateProfileRequest.getFname());
            }
            if (updateProfileRequest.getLname() != null){
                foundSP.setLname(updateProfileRequest.getLname());
            }
            if (updateProfileRequest.getPassword() != null){
                foundSP.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));
            }
            if (updateProfileRequest.getPhone() != null){
                foundSP.setPhone(updateProfileRequest.getPhone());
            }
            userRepository.save(foundSP);
            serviceProviderRepository.save(foundSP);
            return foundSP;
        }
    }

    public ServiceProvider viewServiceProviderProfile(String email) {
        ServiceProvider foundSP = serviceProviderRepository.findByEmail(email).orElse(null);
        if (foundSP == null){
            throw new NoSuchElementException("Service Provider with email " + email + "is not found.");
        }
        else{
            return foundSP;
        }
    }

    public Object activateAccount(String email) {
        User foundUser = userRepository.findByEmail(email).orElse(null);
        if (foundUser == null){
            throw new NoSuchElementException("User with email " + email + " is not found.");
        }
        if (foundUser.getAccountState() == "activated"){
            return "Account is already activated";
        }
        foundUser.setAccountState("activated");
        userRepository.save(foundUser);
        return "Account is activated successfully!";
    }

    public Object deactivateAccount(String email) {
        User foundUser = userRepository.findByEmail(email).orElse(null);
        if (foundUser == null){
            throw new NoSuchElementException("User with email " + email + " is not found.");
        }
        if (foundUser.getAccountState() == "deactivated"){
            return "Account is already deactivated";
        }
        foundUser.setAccountState("deactivated");
        userRepository.save(foundUser);
        return "Account is deactivated successfully!";
    }

    public Object resetPassword(String email, String password) {
        User foundUser = userRepository.findByEmail(email).orElse(null);
        if (foundUser == null){
            throw new NoSuchElementException("User with email " + email + " is not found.");
        }
        foundUser.setPassword(passwordEncoder.encode(password));
//        foundUser.setPassword(password);
        userRepository.save(foundUser);
        if (foundUser.getRole().equals("SERVICE_PROVIDER")){
            ServiceProvider foundSP = serviceProviderRepository.findByEmail(email).orElse(null);
            foundSP.setPassword(passwordEncoder.encode(password));
            serviceProviderRepository.save(foundSP);
        }
        return "Password is reset successfully!";
    }

    public Object changeServiceProviderSpeciality(String email, String newSpeciality) {
        ServiceProvider foundSP = serviceProviderRepository.findByEmail(email).orElse(null);
        if (foundSP == null){
            throw new NoSuchElementException("Service Provider with email " + email + "is not found.");
        }
        foundSP.setSpeciality(newSpeciality);
        serviceProviderRepository.save(foundSP);
        return "Speciality is changed successfully to " + newSpeciality;
    }

    public Object setSlot(String tokenEmail, LocalDateTime dateTime) {
        ServiceProvider foundSP = serviceProviderRepository.findByEmail(tokenEmail).orElse(null);
        if (foundSP == null){
            throw new NoSuchElementException("Service Provider with email " + tokenEmail + "is not found.");
        }
        LocalDateTime truncatedDateTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
        List<Slots> spSlots = slotRepository.findByServiceProvider(foundSP);
        for (Slots slot : spSlots){
            if (slot.getSlotDate().equals(truncatedDateTime)){
                return "Service Provider with email " + tokenEmail + "is already assigned to a slot at this time.";
            }
        }

        Slots newSlot = new Slots();
        newSlot.setSlotDate(dateTime);
        newSlot.setServiceProvider(foundSP);
        newSlot.setAvailable(true);

        slotRepository.save(newSlot);
        return "Slot added successfully.";
    }

    public Object setSlotAvailability(Integer slotId, boolean available) {
        Slots foundSlot = slotRepository.findById(slotId).orElse(null);

        if (foundSlot == null){
            throw new NoSuchElementException("Slot with id  " + slotId + "is not found.");
        }

        if (available && foundSlot.isAvailable()){
            return "Slot is already available.";
        }
        if (!available && !foundSlot.isAvailable()){
            return "Slot is already unavailable.";
        }
        if (available){
            foundSlot.setAvailable(true);
            slotRepository.save(foundSlot);
            return "Slot availability changed to available successfully.";
        }
        else{
            foundSlot.setAvailable(false);
            slotRepository.save(foundSlot);
            return "Slot availability changed to unavailable successfully.";
        }
    }

    public List<Appointment> viewPatientAppointmentHistory(String email) {
        User patient = userRepository.findByEmail(email).orElse(null);
        if (patient == null){
            throw  new NoSuchElementException("Patient with email " + email + " is not found");
        }
        List<Appointment> patientAppointments = appointmentRepository.findByPatient(patient).orElse(null);
        if (patientAppointments == null){
            throw new NoSuchElementException("No previous appointments to patient with email " + email);
        }
        return patientAppointments;
    }
}
