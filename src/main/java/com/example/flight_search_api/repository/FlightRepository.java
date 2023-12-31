package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    List<Flight> findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeBetween(
            String departureCity,
            String arrivalCity,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    List<Flight> findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeBetweenAndReturnDateTimeBetween(
            String departureCity,
            String arrivalCity,
            LocalDateTime startDepartureDateTime,
            LocalDateTime endDepartureDateTime,
            LocalDateTime startReturnDateTime,
            LocalDateTime endReturnDateTime
    );
}
