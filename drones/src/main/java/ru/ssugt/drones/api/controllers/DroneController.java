package ru.ssugt.drones.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssugt.drones.api.dto.request.drone.DroneAddToDockStationRequest;
import ru.ssugt.drones.api.dto.request.drone.DroneRegisterRequest;
import ru.ssugt.drones.api.dto.response.dockstation.DockStationResponse;
import ru.ssugt.drones.api.dto.response.drone.DroneResponse;
import ru.ssugt.drones.api.dto.response.drone.DronesResponse;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.service.DroneService;

import java.util.List;

@RestController
@RequestMapping("api/v1/drone")
public class DroneController {

    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("/register")
    public ResponseEntity<DroneResponse> registerDrone(@RequestBody DroneRegisterRequest droneRegisterRequest) {
        Drone drone = droneService.register(droneRegisterRequest);
        return ResponseEntity.ok(DroneResponse.builder()
                        .id(drone.getId())
                .build());
    }

    @PostMapping("/add-to-dock-station")
    public ResponseEntity<DockStationResponse> addDroneToDockStation(@RequestBody DroneAddToDockStationRequest droneAddToDockStationRequest) {
        DockStation dockStation = droneService.addToDockStation(droneAddToDockStationRequest);
        return ResponseEntity.ok(DockStationResponse.builder()
                .id(dockStation.getId())
                .build());
    }

    @GetMapping("/get-all")
    public ResponseEntity<DronesResponse> getAllDrones() {
        List<Drone> drones = droneService.getAllDrones();
        return ResponseEntity.ok(DronesResponse.builder()
                .drones(drones)
                .build());
    }
}
