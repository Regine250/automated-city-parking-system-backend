package auca.example.parking.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.SessionStatus;
import auca.example.parking.repository.AttendantRepository;
import auca.example.parking.repository.FeeReceiptRepository;
import auca.example.parking.repository.MotoristRepository;
import auca.example.parking.repository.ParkingLotRepository;
import auca.example.parking.repository.ParkingSessionRepository;

@Service
public class DashboardService {
    @Autowired private MotoristRepository motoristRepo;
    @Autowired private AttendantRepository attendantRepo;
    @Autowired private ParkingLotRepository lotRepo;
    @Autowired private ParkingSessionRepository sessionRepo;
    @Autowired private FeeReceiptRepository receiptRepo;

    public Map<String, Long> getSummary() {
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("motorists", motoristRepo.count());
        m.put("attendants", attendantRepo.count());
        m.put("freeParkingLots", (long) lotRepo.findFreeParkingLots().size());
        m.put("sessionsToday", sessionRepo.countBySessionDate(LocalDate.now()));
        m.put("reserved", sessionRepo.countByStatus(SessionStatus.RESERVED));
        m.put("feeReceipts", receiptRepo.count());
        return m;
    }
}