package ru.ssugt.drones.api.dto.request.drone;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.ssugt.drones.jpa.entities.Location;

@Data
@Getter
@Setter
@Builder
public class DroneRegisterRequest {
    private String model;
}
