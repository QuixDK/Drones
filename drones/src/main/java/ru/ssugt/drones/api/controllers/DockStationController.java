package ru.ssugt.drones.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.api.dto.response.dockstation.DockStationResponse;
import ru.ssugt.drones.api.dto.response.dockstation.DockStationsResponse;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.service.DockStationService;

@RestController
@RequestMapping("api/v1/dock-station")
public class DockStationController {

    private final DockStationService dockStationService;

    @Autowired
    public DockStationController(DockStationService dockStationService) {
        this.dockStationService = dockStationService;
    }

    @PostMapping("/register")
    public ResponseEntity<DockStationResponse> registerDockStation(@RequestBody DockStationRegisterRequest dockStationRegisterRequest) {
        DockStation dockStation = dockStationService.register(dockStationRegisterRequest);
        return ResponseEntity.ok(DockStationResponse.builder()
                .id(dockStation.getId())
                .build());
    }


    @GetMapping("/get-all")
    public ResponseEntity<DockStationsResponse> getAllDockStations() {
        return ResponseEntity.ok(DockStationsResponse.builder().dockStations(dockStationService.getAll()).build());
    }
}
