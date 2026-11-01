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
            if (lot == null)
                return "There isn't parking lot with that lotNumber";
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
        return "An Attendant was saved successfully";
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
            return "There isn't an attendant with that id";
        a.setFirstName(newData.getFirstName());
        a.setLastName(newData.getLastName());
        a.setBadgeNumber(newData.getBadgeNumber());

        if (newData.getParkingLot() != null && newData.getParkingLot().getId() != null) {
            ParkingLot lot = lotRepo.findById(newData.getParkingLot().getId()).orElse(null);
            if (lot == null)
                return "The parking lot with that lotNumber doesn't exist";
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
        return "An Attendant was updated successfully";
    }

    public String deleteAttendant(UUID id) {
        if (!repo.existsById(id))
            return "There isn't attendant with that id";
        repo.deleteById(id);
        return "The Attendant was deleted successfully";
    }

    public String addVehicleType(UUID attendantId, UUID vehicleTypeId) {
        Attendant a = repo.findById(attendantId).orElse(null);
        if (a == null)
            return "There isn't an attendant with that id";
        VehicleType vt = vtRepo.findById(vehicleTypeId).orElse(null);
        if (vt == null)
            return "That vehicle type doesn't exist";
        if (a.getVehicleTypes().contains(vt))
            return "There is an attendant with that vehicle type";
        a.getVehicleTypes().add(vt);
        repo.save(a);
        return "The Vehicle type was added to the attendant";
    }

    public String moveAttendantToParkingLot(UUID attendantId, int lotNumber) {
        Attendant a = repo.findById(attendantId).orElse(null);
        if (a == null)
            return "There is no attendant with that id";
        ParkingLot lot = lotRepo.findByLotNumber(lotNumber).orElse(null);
        if (lot == null)
            return "There is no parking lot with that lotNumber";
        if (repo.existsByParkingLot(lot) && (a.getParkingLot() == null || !a.getParkingLot().getId().equals(lot.getId()))) {
            return "That parking lot is taken by another attendant";
        }
        a.setParkingLot(lot);
        repo.save(a);
        return "The Parking lot was changed successfully";
    }

    public List<Attendant> getAttendantsByVehicleType(String name) {

        return repo.findByVehicleTypeName(name);
    }
}