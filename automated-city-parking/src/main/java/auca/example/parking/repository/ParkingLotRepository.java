package auca.example.parking.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.example.parking.model.domain.ParkingLot;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {
    Optional<ParkingLot> findByLotNumber(int lotNumber);

    @Query("SELECT l FROM ParkingLot l WHERE NOT EXISTS (SELECT a FROM Attendant a WHERE a.parkingLot = l) ORDER BY l.lotNumber ASC")
    List<ParkingLot> findFreeParkingLots();
}