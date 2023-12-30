package com.example.flight_search_api.service;

import com.example.flight_search_api.model.Flight;
import com.example.flight_search_api.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flight not Found"));
    }

    public Flight updateFlight(Long id, Flight flightDetails) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        flight.setDepartureAirport(flightDetails.getDepartureAirport());
        flight.setArrivalAirport(flightDetails.getArrivalAirport());

        return flightRepository.save(flight);
    }

    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        flightRepository.delete(flight);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    public List<Flight> searchOneWayFlights(String departure, String arrival, Date departureDate) {
        return flightRepository.findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTime(
                departure, arrival, departureDate);
    }

    public List<Flight> searchRoundTripFlights(String departure, String arrival, Date departureDate, Date returnDate) {
        return flightRepository.findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeAndReturnDateTime(
                departure, arrival, departureDate, returnDate);
    }
}
