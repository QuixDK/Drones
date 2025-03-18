package ru.ssugt.drones.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FlightMission {
    private Integer droneCount;
    private List<List<Double>> points;
}
