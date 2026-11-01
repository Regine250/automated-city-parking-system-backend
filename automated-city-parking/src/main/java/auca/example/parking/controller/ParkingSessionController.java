package auca.example.parking.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.ParkingSession;
import auca.example.parking.model.domain.SessionStatus;
import auca.example.parking.service.FeeReceiptService;
import auca.example.parking.service.ParkingSessionService;

@RestController
@RequestMapping("/api/parking-sessions")
public class ParkingSessionController {
    @Autowired private ParkingSessionService service;
    @Autowired private FeeReceiptService receiptService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ParkingSession ps) {
        String msg = service.saveParkingSession(ps);
        if (msg.equals("A Parking session was safely saved."))
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        if (msg.equals("A Parking session date can't be in the past"))
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        if (msg.equals("An Attendant already supervises a session on that date"))
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/all")
    public ResponseEntity<?> all() { return new ResponseEntity<>(service.getAllParkingSessions(), HttpStatus.OK); }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        ParkingSession ps = service.getParkingSessionById(id);
        if (ps == null) return new ResponseEntity<>("There is no parking session with that id", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ps, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ParkingSession ps) {
        String msg = service.updateParkingSession(id, ps);
        if (msg.equals("The Parking session was updated successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        ParkingSession ps = service.getParkingSessionById(id);
        if (ps == null) return new ResponseEntity<>("There isn't a parking session with that id", HttpStatus.NOT_FOUND);
        if (receiptService.existsByParkingSession(ps))
            return new ResponseEntity<>("That parking session has fee receipts, delete them first", HttpStatus.CONFLICT);
        String msg = service.deleteParkingSession(id);
        if (msg.equals("The Parking session was safely deleted"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable UUID id, @RequestParam SessionStatus status) {
        String msg = service.updateStatus(id, status);
        if (msg.equals("The Parking session status was updated successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        if (msg.equals("There isn't a parking session with that id"))
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/by-status")
    public ResponseEntity<?> byStatus(@RequestParam SessionStatus status) {
        return new ResponseEntity<>(service.byStatus(status), HttpStatus.OK);
    }
    @GetMapping("/between")
    public ResponseEntity<?> between(@RequestParam String start, @RequestParam String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        return new ResponseEntity<>(service.between(s, e), HttpStatus.OK);
    }
    @GetMapping("/stats/by-status")
    public ResponseEntity<?> stats() {
        return new ResponseEntity<>(service.statsByStatus(), HttpStatus.OK);
    }
    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID attendantId, @RequestParam String date) {
        String msg = service.cancelDay(attendantId, LocalDate.parse(date));
        if (msg.equals("There is no attendant with that id"))
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @GetMapping("/page")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "sessionDate,desc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String prop = parts[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));
        return new ResponseEntity<>(service.getAllParkingSessionsPage(pageable), HttpStatus.OK);
    }
}