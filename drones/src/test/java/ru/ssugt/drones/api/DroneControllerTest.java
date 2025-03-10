package ru.ssugt.drones.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ssugt.drones.api.controllers.DockStationController;
import ru.ssugt.drones.api.controllers.DroneController;
import ru.ssugt.drones.api.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.api.dto.request.DroneAddToDockStationRequest;
import ru.ssugt.drones.api.dto.request.DroneRegisterRequest;
import ru.ssugt.drones.api.dto.response.DockStationResponse;
import ru.ssugt.drones.api.dto.response.DroneResponse;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.jpa.entities.Location;
import ru.ssugt.drones.jpa.repository.DockStationRepository;
import ru.ssugt.drones.jpa.repository.DroneRepository;
import ru.ssugt.drones.service.DockStationService;
import ru.ssugt.drones.service.DroneService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DroneControllerTest {

    @Autowired
    private DroneController droneController;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneService droneService;

    @Autowired
    private DockStationService dockStationService;

    @Autowired
    private DockStationRepository dockStationRepository;

    @Test
    public void testRegisterDroneApi() {
        String droneId = "";
        try {
            DroneRegisterRequest droneRegisterRequest = DroneRegisterRequest.builder()
                    .model("test")
                    .build();
            ResponseEntity<DroneResponse> droneResponseResponseEntity = droneController.registerDrone(droneRegisterRequest);
            Assertions.assertNotNull(droneResponseResponseEntity);
            Assertions.assertNotNull(droneResponseResponseEntity.getBody().getId());
            droneId = droneResponseResponseEntity.getBody().getId();
        } finally {
            droneRepository.deleteById(droneId);
        }
    }

    @Test
    @Transactional
    public void testAddDroneToDockStationApi() {
        String droneId = "";
        String dockStationId = "";
        try {
            DroneRegisterRequest droneRegisterRequest = DroneRegisterRequest.builder()
                    .model("test")
                    .build();
            Drone drone = droneService.register(droneRegisterRequest);
            droneId = drone.getId();

            DockStationRegisterRequest dockStationRegisterRequest = DockStationRegisterRequest.builder()
                    .name("test")
                    .location(Location.builder()
                            .x(1l)
                            .y(1l)
                            .build())
                    .build();
            DockStation dockStation = dockStationService.register(dockStationRegisterRequest);
            dockStationId = dockStation.getId();

            DroneAddToDockStationRequest droneAddToDockStationRequest = DroneAddToDockStationRequest.builder()
                    .droneId(droneId)
                    .dockStationId(dockStationId)
                    .build();

            ResponseEntity<DockStationResponse> dockStationResponseResponseEntity = droneController.addDroneToDockStation(droneAddToDockStationRequest);
            Assertions.assertNotNull(dockStationResponseResponseEntity);
            Assertions.assertNotNull(dockStationResponseResponseEntity.getBody().getId());
            dockStation = dockStationRepository.findById(dockStationId).get();
            Assertions.assertNotNull(dockStation.getDrones());
            Assertions.assertEquals(dockStation.getDrones().size(), 1);
        } finally {
            droneRepository.deleteById(droneId);
            dockStationRepository.deleteById(dockStationId);
        }
    }
}
