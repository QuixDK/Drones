package ru.ssugt.drones.rabbit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneData {
    private String droneId;
    private double latitude;
    private double longitude;
}