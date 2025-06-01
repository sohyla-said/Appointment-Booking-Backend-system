package com.example.SABS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "slots")
public class Slots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotID;

    @Setter
    @ManyToOne
    @JoinColumn(name = "providerID", referencedColumnName = "userID", nullable = false)
    private ServiceProvider serviceProvider;

    @Column(nullable = false)
    //format yyyy-MM-dd'T'HH:mm:ss
    //ex: 2025-04-16T22:30:45.123
    private LocalDateTime slotDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isAvailable;

    public Integer getSlotID() {
        return slotID;
    }

    public void setSlotID(Integer slotID) {
        this.slotID = slotID;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public LocalDateTime getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDateTime slotDate) {
        this.slotDate = slotDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
