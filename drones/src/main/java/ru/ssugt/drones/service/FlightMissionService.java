package ru.ssugt.drones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.jpa.repository.DroneRepository;
import ru.ssugt.drones.rabbit.CentralServer;

import java.io.IOException;
import java.util.List;

@Service
public class FlightMissionService {

    private final CentralServer centralServer;
    private final DroneRepository droneRepository;

    @Autowired
    public FlightMissionService(CentralServer centralServer, DroneRepository droneRepository) {
        this.centralServer = centralServer;
        this.droneRepository = droneRepository;
    }

    @Async
    public void requestDockStationStatus(FlightMission flightMission) throws IOException {
        Drone drone = droneRepository.findById("bd760580-a87a-40b1-a362-4e77c8be3131").get();
        for (List<Double> point: flightMission.getPoints()) {
            drone.getLocation().setLatitude(point.get(0));
            drone.getLocation().setLongitude(point.get(1));
            drone = droneRepository.save(drone);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        List<DockStationCallback> dockStationCallbacks = centralServer.requestStatus();
//        //Поиск ближайшей станции со свободными дронами
//        DockStationCallback dockStationCallback = dockStationCallbacks.get(0);
//        centralServer.sendTask(dockStationCallback.getStationId(), flightMission);

    }

    public FlightMission generateFlightMission(List<List<Double>> points) {
        return FlightMission.builder()
                .points(points)
                .droneCount(0)
                .build();
    }
}
