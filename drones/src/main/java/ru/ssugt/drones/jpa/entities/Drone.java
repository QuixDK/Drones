package ru.ssugt.drones.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Drone extends BaseEntity {

    private String model;

    @Column(
            name = "dockstation_id",
            insertable = false,
            updatable = false
    )
    private String dockStationId;
}
