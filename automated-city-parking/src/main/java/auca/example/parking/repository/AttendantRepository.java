package auca.example.parking.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.example.parking.model.domain.Attendant;
import auca.example.parking.model.domain.ParkingLot;

@Repository
public interface AttendantRepository extends JpaRepository<Attendant, UUID> {
    boolean existsByParkingLot(ParkingLot parkingLot);

    @Query("SELECT a FROM Attendant a JOIN a.vehicleTypes v WHERE LOWER(v.name) = LOWER(:name)")
    List<Attendant> findByVehicleTypeName(@Param("name") String name);
}