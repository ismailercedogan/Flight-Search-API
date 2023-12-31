package com.example.flight_search_api.service;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.model.Flight;
import com.example.flight_search_api.repository.AirportRepository;
import com.example.flight_search_api.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirportService airportService;

    @Autowired
    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository, AirportService airportService) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.airportService = airportService;
    }

    public Flight createFlight(Flight flight) {
        if (flight.getDepartureAirport() != null && flight.getDepartureAirport().getId() == 0) {
            airportRepository.save(flight.getDepartureAirport());
        }
        if (flight.getArrivalAirport() != null && flight.getArrivalAirport().getId() == 0) {
            airportRepository.save(flight.getArrivalAirport());
        }
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
    public List<Flight> searchOneWayFlights(String departureCity, String arrivalCity, Date departureDate) {
        List<Airport> departureAirports = airportService.findAirportsByCity(departureCity);
        List<Airport> arrivalAirports = airportService.findAirportsByCity(arrivalCity);
        return flightRepository.findByDepartureAirportInAndArrivalAirportInAndDepartureDateTime(
                departureAirports, arrivalAirports, departureDate);
    }

    public List<Flight> searchRoundTripFlights(String departureCity, String arrivalCity, Date departureDate, Date returnDate) {
        List<Airport> departureAirports = airportService.findAirportsByCity(departureCity);
        List<Airport> arrivalAirports = airportService.findAirportsByCity(arrivalCity);

        return flightRepository.findByDepartureAirportInAndArrivalAirportInAndDepartureDateTimeAndReturnDateTime(
                departureAirports, arrivalAirports, departureDate, returnDate);
    }
}
