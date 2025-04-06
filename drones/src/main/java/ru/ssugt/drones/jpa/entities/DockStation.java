package ru.ssugt.drones.jpa.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

import java.util.List;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DockStation extends BaseEntity {

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id"
    )
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn (
            name = "dockstation_id",
            referencedColumnName = "id"
    )
    private List<Drone> drones;
    @Column(name = "external_id", unique = true)
    private String externalId;
}
