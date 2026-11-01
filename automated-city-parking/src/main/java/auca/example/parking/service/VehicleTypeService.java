package auca.example.parking.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.example.parking.model.domain.VehicleType;
import auca.example.parking.repository.VehicleTypeRepository;

@Service
public class VehicleTypeService {
    @Autowired private VehicleTypeRepository repo;

    public String saveVehicleType(VehicleType v) { repo.save(v);
        return "Vehicle type saved successfully";
    }
    public List<VehicleType> getAllVehicleTypes() {
        return repo.findAll();
    }
    public VehicleType getVehicleTypeById(UUID id) {
        return repo.findById(id).orElse(null);
    }
    public String updateVehicleType(UUID id, VehicleType newData) {
        VehicleType v = repo.findById(id).orElse(null);
        if (v == null)
            return "The vehicle type with that id does not exist";
        v.setName(newData.getName());
        repo.save(v);
        return "Vehicle type updated successfully";
    }
    public String deleteVehicleType(UUID id) {
        if (!repo.existsById(id))
            return "The vehicle type with that id does not exist";
        repo.deleteById(id);
        return "Vehicle type deleted successfully";
    }
    public List<VehicleType> getUnused() {
        return repo.findUnused();
    }
}