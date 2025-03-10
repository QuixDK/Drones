package ru.ssugt.drones.jpa.repository;

import org.springframework.stereotype.Repository;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;

@Repository
public interface DroneRepository extends BaseRepository<Drone> {
}