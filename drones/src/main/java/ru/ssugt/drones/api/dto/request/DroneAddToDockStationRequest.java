package ru.ssugt.drones.api.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class DroneAddToDockStationRequest {
    private String droneId;
    private String dockStationId;
}
