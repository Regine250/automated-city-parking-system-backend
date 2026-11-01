package auca.example.parking.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.Attendant;
import auca.example.parking.service.AttendantService;

@RestController
@RequestMapping("/api/attendants")
public class AttendantController {
    @Autowired private AttendantService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Attendant a) {
        String msg = service.saveAttendant(a);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.getAllAttendants(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        Attendant a = service.getAttendantById(id);
        if (a == null) return new ResponseEntity<>("The attendant with that id does not exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Attendant a) {
        String msg = service.updateAttendant(id, a);
        if (msg.equals("Attendant updated successfully")) return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = service.deleteAttendant(id);
        if (msg.equals("Attendant deleted successfully")) return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/by-vehicle-type")
    public ResponseEntity<?> byVehicleType(@RequestParam String name) {
        return new ResponseEntity<>(service.getAttendantsByVehicleType(name), HttpStatus.OK);
    }
    @PutMapping("/{attendantId}/vehicle-types/{vehicleTypeId}")
    public ResponseEntity<?> addVehicleType(@PathVariable UUID attendantId, @PathVariable UUID vehicleTypeId) {
        String msg = service.addVehicleType(attendantId, vehicleTypeId);
        if (msg.equals("Vehicle type added to the attendant"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        if (msg.equals("The attendant with that id does not exist") || msg.equals("That vehicle type does not exist"))
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        if (msg.equals("The attendant already has that vehicle type"))
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
    @PatchMapping("/{attendantId}/parking-lot")
    public ResponseEntity<?> moveParkingLot(@PathVariable UUID attendantId, @RequestParam int lotNumber) {
        String msg = service.moveAttendantToParkingLot(attendantId, lotNumber);
        if (msg.equals("Parking lot changed successfully")) return new ResponseEntity<>(msg, HttpStatus.OK);
        if (msg.equals("The attendant with that id does not exist") || msg.equals("The parking lot with that lotNumber does not exist"))
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        if (msg.equals("That parking lot is taken by another attendant")) return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
}