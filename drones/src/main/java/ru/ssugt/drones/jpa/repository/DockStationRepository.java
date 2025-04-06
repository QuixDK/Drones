package ru.ssugt.drones.jpa.repository;

import org.springframework.stereotype.Repository;
import ru.ssugt.drones.jpa.entities.DockStation;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

import java.util.Optional;

@Repository
public interface DockStationRepository extends BaseRepository<DockStation> {

    boolean existsByExternalId(String externalId);
    Optional<DockStation> findByExternalId(String externalId);
}