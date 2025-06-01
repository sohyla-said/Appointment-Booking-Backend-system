package com.example.SABS.repository;

import com.example.SABS.model.ServiceProvider;
import com.example.SABS.model.Slots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slots, Integer> {


    List<Slots> findByServiceProvider(ServiceProvider provider);

    List<Slots> findByIsAvailableTrue();
}
