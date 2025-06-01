package com.example.SABS.repository;

import com.example.SABS.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {

    Optional<ServiceProvider> findByEmail(String email);
}
