package com.example.flight_search_api.service;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.model.Flight;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class FlightDataFetcherService {

    private final FlightService flightService;
    private final AirportService airportService;

    public FlightDataFetcherService(FlightService flightService, AirportService airportService) {
        this.flightService = flightService;
        this.airportService = airportService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchFlightData() {
        List<Flight> fetchedFlights = simulateFetchingFlightData();
        for (Flight flight : fetchedFlights) {
            Airport departureAirport = airportService.findOrCreateAirport(flight.getDepartureAirport().getCity());
            Airport arrivalAirport = airportService.findOrCreateAirport(flight.getArrivalAirport().getCity());

            flight.setDepartureAirport(departureAirport);
            flight.setArrivalAirport(arrivalAirport);

            flightService.createFlight(flight);
        }
    }

    private List<Flight> simulateFetchingFlightData() {
        List<Flight> flights = new ArrayList<>();
        String[] airports = {"JFK", "ANK", "IST", "ORD", "ATL", "DNZ", "SAW", "MIL", "MDR", "BRC"};
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

        for (int i = 0; i < 10; i++) {
            Flight flight = new Flight();

            Airport departureAirport = new Airport();
            departureAirport.setCity(airports[random.nextInt(airports.length)]);

            flight.setDepartureAirport(departureAirport);

            Airport arrivalAirport = new Airport();
            do {
                arrivalAirport.setCity(airports[random.nextInt(airports.length)]);
            } while (arrivalAirport.getCity().equals(departureAirport.getCity()));
            flight.setArrivalAirport(arrivalAirport);


            LocalDateTime departureTime = LocalDateTime.now();
            LocalDateTime returnTime = departureTime.plusDays(1);

            flight.setDepartureDateTime(departureTime);
            flight.setReturnDateTime(returnTime);
            flight.setPrice(100 + random.nextDouble() * 900);

            flights.add(flight);
        }
        return flights;
    }
}

