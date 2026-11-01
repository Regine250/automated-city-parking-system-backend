package auca.example.parking.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.FeeReceipt;
import auca.example.parking.model.domain.ParkingSession;
import auca.example.parking.model.domain.SessionStatus;
import auca.example.parking.repository.FeeReceiptRepository;
import auca.example.parking.repository.ParkingSessionRepository;

@Service
public class FeeReceiptService {
    @Autowired private FeeReceiptRepository repo;
    @Autowired private ParkingSessionRepository sessionRepo;

    public String saveFeeReceipt(UUID parkingSessionId, FeeReceipt fr) {
        ParkingSession ps = sessionRepo.findById(parkingSessionId).orElse(null);
        if (ps == null)
            return "The parking session with that id does not exist";
        if (ps.getStatus() != SessionStatus.EXITED)
            return "Fee receipts are only for EXITED parking sessions";
        fr.setIssuedDate(LocalDate.now());
        fr.setParkingSession(ps);
        repo.save(fr);
        return "Fee receipt saved successfully";
    }

    public List<FeeReceipt> getByMotorist(String plateNumber) {

        return repo.findByMotoristPlateNumber(plateNumber);
    }

    public String deleteFeeReceipt(UUID id) {
        if (!repo.existsById(id)) return "The fee receipt with that id does not exist";
        repo.deleteById(id);
        return "Fee receipt deleted successfully";
    }

    public boolean existsByParkingSession(ParkingSession ps) {

        return repo.existsByParkingSession(ps);
    }
}