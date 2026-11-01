package auca.example.parking.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.Motorist;
import auca.example.parking.repository.AttendantRepository;
import auca.example.parking.repository.MotoristRepository;
import auca.example.parking.repository.ParkingSessionRepository;

@Service
public class MotoristService {
    @Autowired private MotoristRepository repo;
    @Autowired private AttendantRepository attendantRepo;
    @Autowired private ParkingSessionRepository sessionRepo;

    public String saveMotorist(Motorist m) { repo.save(m);
        return "Motorist saved successfully";
    }
    public List<Motorist> getAllMotorists() {
        return repo.findAll();
    }
    public Motorist getMotoristById(UUID id) {
        return repo.findById(id).orElse(null);
    }
    public String updateMotorist(UUID id, Motorist newData) {
        Motorist m = repo.findById(id).orElse(null);
        if (m == null)
            return "The motorist with that id does not exist";
        m.setFirstName(newData.getFirstName());
        m.setLastName(newData.getLastName());
        m.setPlateNumber(newData.getPlateNumber());
        m.setPhone(newData.getPhone());
        repo.save(m);
        return "Motorist updated successfully";
    }
    public String deleteMotorist(UUID id) {
        if (!repo.existsById(id))
            return "The motorist with that id does not exist";
        repo.deleteById(id);
        return "Motorist deleted successfully";
    }
    public List<Motorist> findByLastName(String lastName) {
        return repo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Motorist> getMotoristsOfAttendant(UUID attendantId) {
        if (!attendantRepo.existsById(attendantId)) return null;
        return sessionRepo.findMotoristsByAttendantId(attendantId);
    }
}