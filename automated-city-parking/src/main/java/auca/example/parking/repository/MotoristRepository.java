package auca.example.parking.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auca.example.parking.model.domain.Motorist;

@Repository
public interface MotoristRepository extends JpaRepository<Motorist, UUID> {
    List<Motorist> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    Optional<Motorist> findByPlateNumber(String plateNumber);
}