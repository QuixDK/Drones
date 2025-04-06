package ru.ssugt.drones.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Location extends BaseEntity {

    private double latitude;
    private double longitude;

}
