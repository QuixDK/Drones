package ru.ssugt.drones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.rabbit.CentralServer;
import ru.ssugt.drones.rabbit.dto.DockStationCallback;

import java.io.IOException;
import java.util.List;

@Service
public class FlightMissionService {

    private final CentralServer centralServer;

    @Autowired
    public FlightMissionService(CentralServer centralServer) {
        this.centralServer = centralServer;
    }

    @Async
    public void requestDockStationStatus(FlightMission flightMission) throws IOException {
        List<DockStationCallback> dockStationCallbacks = centralServer.requestStatus();
        //Поиск ближайшей станции со свободными дронами
        DockStationCallback dockStationCallback = dockStationCallbacks.get(0);
        centralServer.sendTask(dockStationCallback.getStationId(), flightMission);

    }

    public FlightMission generateFlightMission(List<List<Double>> points) {
        return FlightMission.builder()
                .points(points)
                .droneCount(0)
                .build();
    }
}
