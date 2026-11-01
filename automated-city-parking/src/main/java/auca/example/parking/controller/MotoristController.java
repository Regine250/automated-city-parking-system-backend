package auca.example.parking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.Motorist;
import auca.example.parking.service.MotoristService;

@RestController
@RequestMapping("/api/motorists")
public class MotoristController {
    @Autowired private MotoristService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Motorist m) {
        String msg = service.saveMotorist(m);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(service.getAllMotorists(), HttpStatus.OK); }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        Motorist m = service.getMotoristById(id);
        if (m == null)
            return new ResponseEntity<>("The motorist with that id doesn't exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Motorist m) {
        String msg = service.updateMotorist(id, m);
        if (msg.equals("The Motorist was updated successfully"))
    return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = service.deleteMotorist(id);
        if (msg.equals("A Motorist was deleted successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/by-last-name")
    public ResponseEntity<?> byLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(service.findByLastName(lastName), HttpStatus.OK);
    }
    @GetMapping("/of-attendant/{attendantId}")
    public ResponseEntity<?> ofAttendant(@PathVariable UUID attendantId) {
        List<Motorist> result = service.getMotoristsOfAttendant(attendantId);
        if (result == null)
            return new ResponseEntity<>("There is no attendant with that id", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}