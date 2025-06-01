package com.example.SABS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer appointmentID;

    @ManyToOne
    @JoinColumn(name = "providerID", referencedColumnName = "userID", nullable = false)
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn(name = "patientID", referencedColumnName = "userID", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "slotID", referencedColumnName = "slotID", nullable = false)
    private Slots slot;

//    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
//    private boolean is_available;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 'available'")
    private String status;


    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Slots getSlot() {
        return slot;
    }

    public void setSlot(Slots slot) {
        this.slot = slot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
