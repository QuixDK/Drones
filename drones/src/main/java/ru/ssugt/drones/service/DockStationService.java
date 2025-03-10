package ru.ssugt.drones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.repository.DockStationRepository;

@Component
public class DockStationService {

    private final DockStationRepository dockStationRepository;

    @Autowired
    public DockStationService(DockStationRepository dockStationRepository) {
        this.dockStationRepository = dockStationRepository;
    }

    public DockStation register(DockStationRegisterRequest request) {
        return dockStationRepository.save(DockStation.builder()
                        .name(request.getName())
                        .location(request.getLocation())
                .build());
    }
}
