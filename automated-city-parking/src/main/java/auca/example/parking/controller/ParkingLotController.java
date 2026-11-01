package auca.example.parking.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.ParkingLot;
import auca.example.parking.service.ParkingLotService;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {
    @Autowired private ParkingLotService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ParkingLot p) {
        String msg = service.saveParkingLot(p);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.getAllParkingLots(), HttpStatus.OK); }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        ParkingLot p = service.getParkingLotById(id);
        if (p == null)
            return new ResponseEntity<>("There is no parking lot with that id", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ParkingLot p) {
        String msg = service.updateParkingLot(id, p);
        if (msg.equals("A Parking lot was updated successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = service.deleteParkingLot(id);
        if (msg.equals("The Parking lot was deleted successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/free")
    public ResponseEntity<?> free() {
        return new ResponseEntity<>(service.getFreeParkingLots(), HttpStatus.OK);
    }
}