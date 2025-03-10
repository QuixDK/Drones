package ru.ssugt.drones.jpa.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity {

    private long x;
    private long y;

}
