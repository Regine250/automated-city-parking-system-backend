package auca.example.parking.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.example.parking.model.domain.FeeReceipt;
import auca.example.parking.model.domain.ParkingSession;

@Repository
public interface FeeReceiptRepository extends JpaRepository<FeeReceipt, UUID> {
    List<FeeReceipt> findByParkingSession(ParkingSession parkingSession);

    boolean existsByParkingSession(ParkingSession parkingSession);

    @Query("SELECT f FROM FeeReceipt f WHERE f.parkingSession.motorist.plateNumber = :plateNumber ORDER BY f.issuedDate DESC")
    List<FeeReceipt> findByMotoristPlateNumber(@Param("plateNumber") String plateNumber);
}