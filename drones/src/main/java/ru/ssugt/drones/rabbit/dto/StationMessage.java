package ru.ssugt.drones.rabbit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StationMessage {
    private String stationId;
    private double stationLatitude;
    private double stationLongitude;
    private List<DroneData> drones;
    private long timestamp;
}
