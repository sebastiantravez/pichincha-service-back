package com.pichincha.service.repository;

import com.pichincha.service.entity.Movements;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovementsRepository extends CrudRepository<Movements, UUID> {
}
