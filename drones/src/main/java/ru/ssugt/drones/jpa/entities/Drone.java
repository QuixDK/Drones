package ru.ssugt.drones.jpa.entities;

import jakarta.persistence.*;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id"
    )
    private Location location;
    @Column(name = "external_id", unique = true)
    private String externalId;
}
