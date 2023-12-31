package com.example.flight_search_api.service;

import com.example.flight_search_api.model.Airport;
import com.example.flight_search_api.repository.AirportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport getAirport(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airport not Found"));
    }

    public Airport updateAirport(Long id, Airport airportDetails) {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airport not found"));
        airport.setCity(airportDetails.getCity());

        return airportRepository.save(airport);
    }

    public void deleteAirport(Long id) {
        Airport airport = airportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airport not found"));

        airportRepository.delete(airport);
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    public Airport findOrCreateAirport(String city) {
        List<Airport> existingAirports = airportRepository.findByCity(city);
        if (existingAirports.isEmpty()) {
            Airport newAirport = new Airport();
            newAirport.setCity(city);
            return airportRepository.save(newAirport);
        } else {

            return existingAirports.get(0);

    }
    }

    public List<Airport> findAirportsByCity(String city) {
        return airportRepository.findByCity(city);
    }


}
