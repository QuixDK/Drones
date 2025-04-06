package ru.ssugt.drones.jpa.repository;

import org.springframework.stereotype.Repository;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.Drone;

import java.util.Optional;

@Repository
public interface DroneRepository extends BaseRepository<Drone> {
    boolean existsByExternalId(String externalId);
    Optional<Drone> findByExternalId(String externalId);
}