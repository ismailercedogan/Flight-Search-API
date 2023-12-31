package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport,Long> {

    List<Airport> findByCity(String city);
}
