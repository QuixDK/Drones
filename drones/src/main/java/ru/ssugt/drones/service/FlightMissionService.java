package ru.ssugt.drones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.jpa.entities.Location;
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
        if (flightMission.getPoints().isEmpty()) {
            return;
        }

        // Берем первую точку маршрута для поиска ближайшего дрона
        List<Double> firstPoint = flightMission.getPoints().get(0);
        double targetLat = firstPoint.get(0);
        double targetLon = firstPoint.get(1);

        // Находим ближайшего дрона к первой точке маршрута
        Drone nearestDrone = findNearestDrone(targetLat, targetLon);

        if (nearestDrone == null) {
            throw new RuntimeException("No available drones found");
        }

        // Имитируем движение дрона по точкам маршрута
        for (List<Double> point : flightMission.getPoints()) {
            nearestDrone.getLocation().setLatitude(point.get(0));
            nearestDrone.getLocation().setLongitude(point.get(1));
            nearestDrone = droneRepository.save(nearestDrone);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Drone findNearestDrone(double targetLat, double targetLon) {
        List<Drone> allDrones = droneRepository.findAll();

        Drone nearestDrone = null;
        double minDistance = Double.MAX_VALUE;

        for (Drone drone : allDrones) {
            Location location = drone.getLocation();
            if (location == null) continue;

            double droneLat = location.getLatitude();
            double droneLon = location.getLongitude();

            double distance = Math.sqrt(Math.pow(droneLat - targetLat, 2) +
                    Math.pow(droneLon - targetLon, 2));

            if (distance < minDistance) {
                minDistance = distance;
                nearestDrone = drone;
            }
        }

        return nearestDrone;
    }

    public FlightMission generateFlightMission(List<List<Double>> points) {
        return FlightMission.builder()
                .points(points)
                .droneCount(0)
                .build();
    }
}
