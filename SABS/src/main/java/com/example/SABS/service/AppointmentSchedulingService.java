package com.example.SABS.service;

import com.example.SABS.model.Appointment;
import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.Slots;
import com.example.SABS.model.User;
import com.example.SABS.repository.AppointmentRepository;
import com.example.SABS.repository.ServiceProviderRepository;
import com.example.SABS.repository.SlotRepository;
import com.example.SABS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AppointmentSchedulingService {

    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Slots> getAvailableSlots() {
        List<Slots> availableSlots = slotRepository.findByIsAvailableTrue();
        if (availableSlots.isEmpty()){
            throw  new NoSuchElementException("There is no available slots");
        }
        return availableSlots;
    }


    public List<ServiceProvider> getAvailableServiceProviders() {
        List<Slots> availableSlots = slotRepository.findByIsAvailableTrue();
        if (availableSlots.isEmpty()){
            throw  new NoSuchElementException("There is no available slots");
        }
        List<ServiceProvider> availableProviders = new ArrayList<>();
        for(Slots slot: availableSlots){
            ServiceProvider provider = slot.getServiceProvider();
            if (availableProviders.contains(provider)){
                continue;
            }
            availableProviders.add(provider);
        }
        return availableProviders;
    }

    public String reserveSlot(Integer slotId) {
        Slots slot = slotRepository.findById(slotId).orElse(null);
        if (slot == null){
            throw  new NoSuchElementException("Slot with id " + slotId + " is not found.");
        }
        if (!slot.isAvailable()){
            return "Slot is not available, choose another one";
        }
        slot.setAvailable(false);
        slotRepository.save(slot);
        return "Slot with id " + slotId + " is reserved successfully, please confirm the appointment";
    }

    public String confirmAppointment(Integer slotId, String email) {
        Slots slot = slotRepository.findById(slotId).orElse(null);
        if (slot == null){
            throw  new NoSuchElementException("Slot with id " + slotId + " is not found.");
        }

        User patient = userRepository.findByEmail(email).orElse(null);
        if (patient == null){
            throw  new NoSuchElementException("Patient with email " + email + " is not found.");
        }

        ServiceProvider serviceProvider = slot.getServiceProvider();

        Appointment appointment = new Appointment();
        appointment.setServiceProvider(serviceProvider);
        appointment.setPatient(patient);
        appointment.setSlot(slot);
        appointment.setStatus("confirmed");

        appointmentRepository.save(appointment);

        return "Appointment confirmed successfully";
    }
}
