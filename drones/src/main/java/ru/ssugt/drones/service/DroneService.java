package ru.ssugt.drones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.api.dto.request.DroneAddToDockStationRequest;
import ru.ssugt.drones.api.dto.request.DroneRegisterRequest;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.jpa.repository.DockStationRepository;
import ru.ssugt.drones.jpa.repository.DroneRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class DroneService {

    private final DroneRepository droneRepository;

    private final DockStationRepository dockStationRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository, DockStationRepository dockStationRepository) {
        this.droneRepository = droneRepository;
        this.dockStationRepository = dockStationRepository;
    }

    public Drone register(DroneRegisterRequest request) {
        return droneRepository.save(Drone.builder()
                .model(request.getModel())
                .build());
    }

    public DockStation addToDockStation(DroneAddToDockStationRequest request) {
        Drone drone = droneRepository.findById(request.getDroneId()).orElseThrow(() -> new RuntimeException(String.format("Не найден дрон с id: %s", request.getDroneId())));
        DockStation dockStation = dockStationRepository.findById(request.getDockStationId()).orElseThrow(() -> new RuntimeException(String.format("Не найдена док-станция с id: %s", request.getDockStationId())));

        if (dockStation.getDrones() == null) {
            dockStation.setDrones(new ArrayList<>());
            dockStation.getDrones().add(drone);
        }
        return dockStationRepository.save(dockStation);
    }

}
