package auca.example.parking.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import auca.example.parking.model.domain.Attendant;
import auca.example.parking.model.domain.ParkingSession;
import auca.example.parking.model.domain.SessionStatus;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, UUID> {
    List<ParkingSession> findByStatusOrderBySessionDateAsc(SessionStatus status);

    List<ParkingSession> findBySessionDateBetweenOrderBySessionDateAsc(LocalDate start, LocalDate end);

    boolean existsByAttendantAndSessionDateAndStatusNot(Attendant attendant, LocalDate sessionDate, SessionStatus status);

    long countBySessionDate(LocalDate sessionDate);

    long countByStatus(SessionStatus status);

    @Query("SELECT p.status, COUNT(p) FROM ParkingSession p GROUP BY p.status")
    List<Object[]> countByStatusGrouped();

    @Modifying
    @Transactional
    @Query("UPDATE ParkingSession p SET p.status = auca.example.parking.model.domain.SessionStatus.CANCELLED WHERE p.attendant = :attendant AND p.sessionDate = :date AND p.status <> auca.example.parking.model.domain.SessionStatus.EXITED")
    int cancelDay(@Param("attendant") Attendant attendant, @Param("date") LocalDate date);

    Page<ParkingSession> findAll(Pageable pageable);

    @Query("SELECT DISTINCT p.motorist FROM ParkingSession p WHERE p.attendant.id = :attendantId")
    List<auca.example.parking.model.domain.Motorist> findMotoristsByAttendantId(@Param("attendantId") UUID attendantId);
}