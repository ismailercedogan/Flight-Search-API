package com.example.flight_search_api.service;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.model.Flight;
import com.example.flight_search_api.repository.AirportRepository;
import com.example.flight_search_api.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirportService airportService;

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

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
        LocalDateTime departureDateTime = convertToDateHour(departureDate);
        LocalDateTime startOfHour = departureDateTime.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endOfHour = startOfHour.plusHours(1).minusNanos(1);

        return flightRepository.findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeBetween(
                departureCity, arrivalCity, startOfHour, endOfHour);
    }

    public List<Flight> searchRoundTripFlights(String departureCity, String arrivalCity, Date departureDate, Date returnDate) {
        LocalDateTime departureDateTime = convertToDateHour(departureDate);
        LocalDateTime returnDateTime = convertToDateHour(returnDate);
        LocalDateTime startOfDepartureHour = departureDateTime.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endOfDepartureHour = startOfDepartureHour.plusHours(1).minusNanos(1);
        LocalDateTime startOfReturnHour = returnDateTime.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endOfReturnHour = startOfReturnHour.plusHours(1).minusNanos(1);

        return flightRepository.findByDepartureAirportCityAndArrivalAirportCityAndDepartureDateTimeBetweenAndReturnDateTimeBetween(
                departureCity, arrivalCity, startOfDepartureHour, endOfDepartureHour, startOfReturnHour, endOfReturnHour);
    }

    private LocalDateTime convertToDateHour(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
