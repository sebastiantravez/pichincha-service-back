package com.pichincha.service.repository;

import com.pichincha.service.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
    Optional<Person> findByDni(@NotBlank String dni);

    @Query("SELECT p FROM Person p " +
            "WHERE p.dni like :value OR " +
            "UPPER(p.fullName) like :value")
    List<Person> findByFullNameLikeOrDni(String value);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE dni like CONCAT('%',:searchValue,'%') " +
            "OR LOWER(fullName) like LOWER(CONCAT('%',:searchValue,'%')) ")
    List<Person> findByNameIgnoreCase(String searchValue);
}
