
package auca.example.parking.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import auca.example.parking.model.domain.Attendant;
import auca.example.parking.model.domain.Motorist;
import auca.example.parking.model.domain.ParkingSession;
import auca.example.parking.model.domain.SessionStatus;

import auca.example.parking.repository.AttendantRepository;
import auca.example.parking.repository.MotoristRepository;
import auca.example.parking.repository.ParkingSessionRepository;

@Service
public class ParkingSessionService {

    @Autowired
    private ParkingSessionRepository repo;

    @Autowired
    private AttendantRepository attendantRepo;

    @Autowired
    private MotoristRepository motoristRepo;

    public String saveParkingSession(ParkingSession ps) {

        if (ps.getSessionDate() == null) {
            return "Session date is required";
        }

        if (ps.getSessionDate().isBefore(LocalDate.now())) {
            return "Parking session date cannot be in the past";
        }

        if (ps.getHours() <= 0) {
            return "Parking hours must be greater than zero";
        }

        if (ps.getBayLabel() == null ||
                ps.getBayLabel().trim().isEmpty()) {
            return "Parking bay label is required";
        }

        if (ps.getMotorist() == null ||
                ps.getMotorist().getId() == null) {
            return "Motorist ID is required";
        }

        Motorist motorist = motoristRepo
                .findById(ps.getMotorist().getId())
                .orElse(null);

        if (motorist == null) {
            return "The motorist with that ID does not exist";
        }

        if (ps.getAttendant() == null ||
                ps.getAttendant().getId() == null) {
            return "Attendant ID is required";
        }

        Attendant attendant = attendantRepo
                .findById(ps.getAttendant().getId())
                .orElse(null);

        if (attendant == null) {
            return "The attendant with that ID does not exist";
        }

        if (repo.existsByAttendantAndSessionDateAndStatusNot(
                attendant,
                ps.getSessionDate(),
                SessionStatus.CANCELLED)) {

            return "Attendant already supervises a session on that date";
        }

        ps.setMotorist(motorist);
        ps.setAttendant(attendant);
        ps.setStatus(SessionStatus.RESERVED);

        repo.save(ps);

        return "Parking session saved successfully";
    }
    public List<ParkingSession> getAllParkingSessions() {
        return repo.findAll();
    }

    public Page<ParkingSession> getAllParkingSessionsPage(
            Pageable pageable) {
        return repo.findAll(pageable);
    }
    public ParkingSession getParkingSessionById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public String updateParkingSession(UUID id, ParkingSession newData) {

        ParkingSession ps = repo.findById(id).orElse(null);

        if (ps == null) {
            return "The parking session with that id does not exist";
        }

        ps.setSessionDate(newData.getSessionDate());
        ps.setBayLabel(newData.getBayLabel());
        ps.setHours(newData.getHours());

        if (newData.getMotorist() != null &&
                newData.getMotorist().getId() != null) {

            Motorist motorist = motoristRepo
                    .findById(newData.getMotorist().getId())
                    .orElse(null);

            if (motorist == null) {
                return "The motorist with that ID does not exist";
            }

            ps.setMotorist(motorist);
        }

        if (newData.getAttendant() != null &&
                newData.getAttendant().getId() != null) {

            Attendant attendant = attendantRepo
                    .findById(newData.getAttendant().getId())
                    .orElse(null);

            if (attendant == null) {
                return "The attendant with that ID does not exist";
            }

            ps.setAttendant(attendant);
        }

        repo.save(ps);

        return "Parking session updated successfully";
    }

    public String deleteParkingSession(UUID id) {

        ParkingSession ps = repo.findById(id).orElse(null);

        if (ps == null) {
            return "The parking session with that id does not exist";
        }

        repo.deleteById(id);

        return "Parking session deleted successfully";
    }

    public String updateStatus(UUID id, SessionStatus status) {

        ParkingSession ps = repo.findById(id).orElse(null);

        if (ps == null) {
            return "The parking session with that id does not exist";
        }

        SessionStatus current = ps.getStatus();

        boolean allowed = false;

        if (current == SessionStatus.RESERVED) {
            if (status == SessionStatus.PARKED ||
                    status == SessionStatus.CANCELLED) {
                allowed = true;
            }
        } else if (current == SessionStatus.PARKED) {
            if (status == SessionStatus.EXITED ||
                    status == SessionStatus.CANCELLED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + status;
        }

        ps.setStatus(status);
        repo.save(ps);

        return "Parking session status updated successfully";
    }

    public List<ParkingSession> byStatus(SessionStatus status) {
        return repo.findByStatusOrderBySessionDateAsc(status);
    }

    public List<ParkingSession> between(
            LocalDate start, LocalDate end) {

        return repo.findBySessionDateBetweenOrderBySessionDateAsc(
                start, end);
    }
    public List<Object[]> statsByStatus() {
        return repo.countByStatusGrouped();
    }


    @Transactional
    public String cancelDay(UUID attendantId, LocalDate date) {

        Attendant attendant = attendantRepo
                .findById(attendantId)
                .orElse(null);

        if (attendant == null) {
            return "The attendant with that id does not exist";
        }

        int count = repo.cancelDay(attendant, date);

        return count + " parking sessions cancelled";
    }
}
