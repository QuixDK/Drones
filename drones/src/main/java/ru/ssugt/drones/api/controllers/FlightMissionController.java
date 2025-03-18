package ru.ssugt.drones.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.service.FlightMissionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flight-mission")
public class FlightMissionController {


    private final FlightMissionService flightMissionService;

    @Autowired
    public FlightMissionController(FlightMissionService flightMissionService) {
        this.flightMissionService = flightMissionService;
    }


    @PostMapping("/load")
    public String loadFlightMission(@RequestBody List<List<Double>> points) throws IOException {
        FlightMission flightMission = flightMissionService.generateFlightMission(points);
        flightMissionService.requestDockStationStatus(flightMission);
        return "Points received successfully!";
    }
}
