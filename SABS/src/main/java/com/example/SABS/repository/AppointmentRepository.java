package com.example.SABS.repository;

import com.example.SABS.model.Appointment;
import com.example.SABS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<List<Appointment>> findByPatient(User user);
}
