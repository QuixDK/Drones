package ru.ssugt.drones.api.dto.response.dockstation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssugt.drones.jpa.entities.DockStation;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DockStationsResponse {

    private List<DockStation> dockStations;

}
