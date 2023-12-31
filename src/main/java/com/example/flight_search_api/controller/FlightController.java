package com.example.flight_search_api.controller;

import com.example.flight_search_api.model.Flight;
import com.example.flight_search_api.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd-HH") String departureDateTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH") String returnDateTime) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");

            Date departureDate = formatter.parse(departureDateTime);
            Date returnDate = (returnDateTime != null && !returnDateTime.isEmpty()) ? formatter.parse(returnDateTime) : null;

            if (departureCity == null || arrivalCity == null || departureDate == null) {
                return ResponseEntity.badRequest().build();
            }

            if (returnDate == null) {
                List<Flight> oneWayFlights = flightService.searchOneWayFlights(departureCity, arrivalCity, departureDate);
                return ResponseEntity.ok(oneWayFlights);
            } else {
                List<Flight> roundTripFlights = flightService.searchRoundTripFlights(departureCity, arrivalCity, departureDate, returnDate);
                return ResponseEntity.ok(roundTripFlights);
            }
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


