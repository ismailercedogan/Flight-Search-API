package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight,Long> {
}
