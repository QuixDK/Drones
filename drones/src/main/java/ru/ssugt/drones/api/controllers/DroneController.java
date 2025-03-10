package ru.ssugt.drones.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.api.dto.request.DroneAddToDockStationRequest;
import ru.ssugt.drones.api.dto.request.DroneRegisterRequest;
import ru.ssugt.drones.api.dto.response.DockStationResponse;
import ru.ssugt.drones.api.dto.response.DroneResponse;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.service.DockStationService;
import ru.ssugt.drones.service.DroneService;

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
}
