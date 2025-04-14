package ru.ssugt.drones.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightMissionDto {
    private List<List<Double>> points;
    private FlightSettings settings;
}
