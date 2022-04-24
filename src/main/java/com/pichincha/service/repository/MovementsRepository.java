package com.pichincha.service.repository;

import com.pichincha.service.entity.Movements;
import com.pichincha.service.enums.TransactionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovementsRepository extends CrudRepository<Movements, UUID> {
    @Query(value = "SELECT m.* FROM movements m " +
            "WHERE m.account_id = :accountId " +
            "AND m.transaction_type = :transactionType " +
            "ORDER BY m.movement_date DESC LIMIT 1", nativeQuery = true)
    Optional<Movements> findLastMove(UUID accountId, TransactionType transactionType);

    @Query("SELECT m FROM Movements m ORDER BY m.movementDate DESC")
    List<Movements> finAllMovements();
}
