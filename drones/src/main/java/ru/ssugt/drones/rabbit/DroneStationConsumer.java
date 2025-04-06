package ru.ssugt.drones.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;
import ru.ssugt.drones.jpa.entities.Location;
import ru.ssugt.drones.jpa.repository.DockStationRepository;
import ru.ssugt.drones.jpa.repository.DroneRepository;
import ru.ssugt.drones.rabbit.dto.DroneData;
import ru.ssugt.drones.rabbit.dto.StationMessage;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Component
public class DroneStationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DroneStationConsumer.class);

    private final ObjectMapper objectMapper;
    private final DockStationRepository dockStationRepository;
    private final DroneRepository droneRepository;

    public DroneStationConsumer(ObjectMapper objectMapper, DockStationRepository dockStationRepository, DroneRepository droneRepository) {
        this.objectMapper = objectMapper;
        this.dockStationRepository = dockStationRepository;
        this.droneRepository = droneRepository;
    }

    @RabbitListener(queues = "${station.queue.name}")
    @Transactional
    public void receiveMessage(String message) {
        try {
            StationMessage stationMessage = objectMapper.readValue(message, StationMessage.class);

            logger.info("Received message from station: {}", stationMessage.getStationId());
            logger.info("Station coordinates: lat={}, long={}",
                    stationMessage.getStationLatitude(),
                    stationMessage.getStationLongitude());

            if (dockStationRepository.existsByExternalId(stationMessage.getStationId())) {
                DockStation dockStation = dockStationRepository.findByExternalId(stationMessage.getStationId()).get();
                dockStation.getLocation().setLatitude(stationMessage.getStationLatitude());
                dockStation.getLocation().setLongitude(stationMessage.getStationLongitude());
                dockStation.setDrones(parseDronesFromMessage(stationMessage));
                dockStationRepository.save(dockStation);
            }
            else {
                DockStation dockStation = new DockStation();
                dockStation.setLocation(new Location(stationMessage.getStationLatitude(), stationMessage.getStationLongitude()));
                dockStation.setDrones(parseDronesFromMessage(stationMessage));
                dockStation.setExternalId(stationMessage.getStationId());
                dockStationRepository.save(dockStation);
            }

        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
            throw new RuntimeException("Failed to process message", e);
        }
    }

    private List<Drone> parseDronesFromMessage(StationMessage stationMessage) {
        List<Drone> droneList = new ArrayList<>();
        for (DroneData messageDrone: stationMessage.getDrones()) {
            if (droneRepository.existsByExternalId(messageDrone.getDroneId())) {
                Drone drone = droneRepository.findByExternalId(messageDrone.getDroneId()).get();
                drone.getLocation().setLongitude(messageDrone.getLongitude());
                drone.getLocation().setLatitude(messageDrone.getLatitude());
                droneList.add(drone);
            }
            else {
                Drone drone = new Drone();
                drone.setLocation(new Location(messageDrone.getLatitude(), messageDrone.getLongitude()));
                drone.setExternalId(messageDrone.getDroneId());
                droneList.add(drone);
            }
        }
        return droneList;
    }
}