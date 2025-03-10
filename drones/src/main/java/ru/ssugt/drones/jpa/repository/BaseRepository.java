package ru.ssugt.drones.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ssugt.drones.jpa.entities.common.BaseEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepository<T extends BaseEntity> extends CrudRepository<T, String> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    Optional<T> findById(String id);

    boolean existsById(String id);

    Iterable<T> findAll();

    long count();

    void deleteById(String id);

    Iterable<T> findAllById(Iterable<String> ids);
}
