package ru.ssugt.drones.api.dto.response.drone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssugt.drones.jpa.entities.Drone;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DronesResponse {

    private List<Drone> drones;

}
