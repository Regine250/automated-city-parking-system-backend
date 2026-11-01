package auca.example.parking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.VehicleType;
import auca.example.parking.service.VehicleTypeService;

@RestController
@RequestMapping("/api/vehicle-types")
public class VehicleTypeController {
    @Autowired private VehicleTypeService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody VehicleType v) {
        String msg = service.saveVehicleType(v);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.getAllVehicleTypes(), HttpStatus.OK); }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        VehicleType v = service.getVehicleTypeById(id);
        if (v == null)
            return new ResponseEntity<>("The vehicle type with that id does not exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(v, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody VehicleType v) {
        String msg = service.updateVehicleType(id, v);
        if (msg.equals("Vehicle type updated successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = service.deleteVehicleType(id);
        if (msg.equals("Vehicle type deleted successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/unused")
    public ResponseEntity<?> unused() {
        return new ResponseEntity<>(service.getUnused(), HttpStatus.OK);
    }
}