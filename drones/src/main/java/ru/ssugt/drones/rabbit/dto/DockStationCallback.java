package ru.ssugt.drones.rabbit.dto;

import lombok.Data;
import ru.ssugt.drones.jpa.entities.Location;

@Data
public class DockStationCallback {
    private String stationId;
    private Integer freeDroneCount;
    private Location location;
}
