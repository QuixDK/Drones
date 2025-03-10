package ru.ssugt.drones.api;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @PostMapping("/savePoints")
    public String savePoints(@RequestBody List<List<Double>> points) {

        points.forEach(point -> System.out.println("Latitude: " + point.get(0) + ", Longitude: " + point.get(1)));

        return "Points received successfully!";
    }
}
