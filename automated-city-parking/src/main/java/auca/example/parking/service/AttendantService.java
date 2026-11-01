package auca.example.parking.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.Attendant;
import auca.example.parking.model.domain.ParkingLot;
import auca.example.parking.model.domain.VehicleType;
import auca.example.parking.repository.AttendantRepository;
import auca.example.parking.repository.ParkingLotRepository;
import auca.example.parking.repository.VehicleTypeRepository;

@Service
public class AttendantService {
    @Autowired private AttendantRepository repo;
    @Autowired private ParkingLotRepository lotRepo;
    @Autowired private VehicleTypeRepository vtRepo;

    public String saveAttendant(Attendant a) {
        if (a.getParkingLot() != null && a.getParkingLot().getId() != null) {
            ParkingLot lot = lotRepo.findById(a.getParkingLot().getId()).orElse(null);
            if (lot == null) return "The parking lot with that lotNumber does not exist";
            a.setParkingLot(lot);
        }
        if (a.getVehicleTypes() != null && !a.getVehicleTypes().isEmpty()) {
            java.util.List<VehicleType> managed = new java.util.ArrayList<>();
            for (VehicleType vt : a.getVehicleTypes()) {
                if (vt.getId() != null) {
                    VehicleType existing = vtRepo.findById(vt.getId()).orElse(null);
                    if (existing != null) managed.add(existing);
                }
            }
            a.setVehicleTypes(managed);
        }
        repo.save(a);
        return "Attendant saved successfully";
    }

    public List<Attendant> getAllAttendants() {
        return repo.findAll();
    }
    public Attendant getAttendantById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public String updateAttendant(UUID id, Attendant newData) {
        Attendant a = repo.findById(id).orElse(null);
        if (a == null)
            return "The attendant with that id does not exist";
        a.setFirstName(newData.getFirstName());
        a.setLastName(newData.getLastName());
        a.setBadgeNumber(newData.getBadgeNumber());

        if (newData.getParkingLot() != null && newData.getParkingLot().getId() != null) {
            ParkingLot lot = lotRepo.findById(newData.getParkingLot().getId()).orElse(null);
            if (lot == null)
                return "The parking lot with that lotNumber does not exist";
            a.setParkingLot(lot);
        } else if (newData.getParkingLot() == null) {
            a.setParkingLot(null);
        }
        if (newData.getVehicleTypes() != null) {
            java.util.List<VehicleType> managed = new java.util.ArrayList<>();
            for (VehicleType vt : newData.getVehicleTypes()) {
                if (vt.getId() != null) {
                    VehicleType existing = vtRepo.findById(vt.getId()).orElse(null);
                    if (existing != null) managed.add(existing);
                }
            }
            a.setVehicleTypes(managed);
        }
        repo.save(a);
        return "Attendant updated successfully";
    }

    public String deleteAttendant(UUID id) {
        if (!repo.existsById(id)) return "The attendant with that id does not exist";
        repo.deleteById(id);
        return "Attendant deleted successfully";
    }

    public String addVehicleType(UUID attendantId, UUID vehicleTypeId) {
        Attendant a = repo.findById(attendantId).orElse(null);
        if (a == null)
            return "The attendant with that id does not exist";
        VehicleType vt = vtRepo.findById(vehicleTypeId).orElse(null);
        if (vt == null)
            return "That vehicle type does not exist";
        if (a.getVehicleTypes().contains(vt))
            return "The attendant already has that vehicle type";
        a.getVehicleTypes().add(vt);
        repo.save(a);
        return "Vehicle type added to the attendant";
    }

    public String moveAttendantToParkingLot(UUID attendantId, int lotNumber) {
        Attendant a = repo.findById(attendantId).orElse(null);
        if (a == null)
            return "The attendant with that id does not exist";
        ParkingLot lot = lotRepo.findByLotNumber(lotNumber).orElse(null);
        if (lot == null)
            return "The parking lot with that lotNumber does not exist";
        if (repo.existsByParkingLot(lot) && (a.getParkingLot() == null || !a.getParkingLot().getId().equals(lot.getId()))) {
            return "That parking lot is taken by another attendant";
        }
        a.setParkingLot(lot);
        repo.save(a);
        return "Parking lot changed successfully";
    }

    public List<Attendant> getAttendantsByVehicleType(String name) {

        return repo.findByVehicleTypeName(name);
    }
}