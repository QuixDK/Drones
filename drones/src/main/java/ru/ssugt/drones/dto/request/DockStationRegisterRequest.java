package ru.ssugt.drones.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.ssugt.drones.jpa.entities.Location;

@Data
@Getter
@Setter
@Builder
public class DockStationRegisterRequest {
    private String name;
    private Location location;
}
