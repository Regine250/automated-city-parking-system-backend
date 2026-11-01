package auca.example.parking.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.example.parking.model.domain.VehicleType;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, UUID> {
    @Query("SELECT v FROM VehicleType v WHERE v.attendants IS EMPTY")
    List<VehicleType> findUnused();
}