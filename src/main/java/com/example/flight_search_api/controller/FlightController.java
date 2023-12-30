package com.example.flight_search_api.controller;

import com.example.flight_search_api.model.Flight;
import com.example.flight_search_api.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightService.createFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        Flight flight = flightService.getFlight(id);
        return ResponseEntity.ok(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
        Flight updatedFlight = flightService.updateFlight(id, flightDetails);
        return ResponseEntity.ok(updatedFlight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate) {

        if (departure == null || arrival == null || departureDate == null) {
            return ResponseEntity.badRequest().build();
        }

        if (returnDate == null) {
            List<Flight> oneWayFlights = flightService.searchOneWayFlights(departure, arrival, departureDate);
            return ResponseEntity.ok(oneWayFlights);
        } else {
            List<Flight> roundTripFlights = flightService.searchRoundTripFlights(departure, arrival, departureDate, returnDate);
            return ResponseEntity.ok(roundTripFlights);
        }
    }
}


