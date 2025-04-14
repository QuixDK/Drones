package ru.ssugt.drones.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightSettings {
    String height;
    String speed;
    String lineSpacing;
    String scanDensity;
    String overlap;
}
