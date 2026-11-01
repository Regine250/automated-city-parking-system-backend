package auca.example.parking.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.ParkingLot;
import auca.example.parking.repository.ParkingLotRepository;

@Service
public class ParkingLotService {
    @Autowired private ParkingLotRepository repo;

    public String saveParkingLot(ParkingLot p) { repo.save(p);
        return "Parking lot was saved successfully";
    }
    public List<ParkingLot> getAllParkingLots() {
        return repo.findAll();
    }
    public ParkingLot getParkingLotById(UUID id) {
        return repo.findById(id).orElse(null);
    }
    public String updateParkingLot(UUID id, ParkingLot newData) {
        ParkingLot p = repo.findById(id).orElse(null);
        if (p == null)
            return "The parking lot with that id doesn't exist";
        p.setLotNumber(newData.getLotNumber());
        p.setName(newData.getName());
        repo.save(p);
        return "Parking lot updated successfully";
    }
    public String deleteParkingLot(UUID id) {
        if (!repo.existsById(id))
            return "The parking lot with that id doesn't exist";
        repo.deleteById(id);
        return "A Parking lot was deleted successfully";
    }
    public List<ParkingLot> getFreeParkingLots() {
        return repo.findFreeParkingLots();
    }
}