package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,Long> {
    List<Flight> findByDepartureAirportInAndArrivalAirportInAndDepartureDateTime(
            List<Airport> departureAirports, List<Airport> arrivalAirports, Date departureDateTime);

    List<Flight> findByDepartureAirportInAndArrivalAirportInAndDepartureDateTimeAndReturnDateTime(
            List<Airport> departureAirports, List<Airport> arrivalAirports, Date departureDateTime, Date returnDateTime);
}
