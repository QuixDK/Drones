package ru.ssugt.drones.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ssugt.drones.dto.request.DockStationRegisterRequest;
import ru.ssugt.drones.dto.response.DockStationResponse;
import ru.ssugt.drones.jpa.entities.Location;
import ru.ssugt.drones.jpa.repository.DockStationRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DockStationControllerTest {

    @Autowired
    private DockStationController dockStationController;

    @Autowired
    private DockStationRepository dockStationRepository;

    @Test
    public void testRegisterDockStationApi() {
        String dockStationId = "";
        try {
            DockStationRegisterRequest dockStationRegisterRequest = DockStationRegisterRequest.builder()
                    .name("test")
                    .location(Location.builder()
                            .x(1l)
                            .y(1l)
                            .build())
                    .build();
            ResponseEntity<DockStationResponse> dockStationResponseResponseEntity = dockStationController.registerDockStation(dockStationRegisterRequest);
            Assertions.assertNotNull(dockStationResponseResponseEntity);
            Assertions.assertNotNull(dockStationResponseResponseEntity.getBody().getId());
            dockStationId = dockStationResponseResponseEntity.getBody().getId();
        } finally {
            dockStationRepository.deleteById(dockStationId);
        }
    }
}
