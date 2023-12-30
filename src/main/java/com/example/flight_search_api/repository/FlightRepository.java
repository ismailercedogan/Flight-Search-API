package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    List<Flight> findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTime(
            String departure, String arrival, Date departureDateTime);

    List<Flight> findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeAndReturnDateTime(
            String departure, String arrival, Date departureDateTime, Date returnDateTime);
}
