package auca.example.parking.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.example.parking.model.domain.FeeReceipt;
import auca.example.parking.service.FeeReceiptService;

@RestController
@RequestMapping("/api/fee-receipts")
public class FeeReceiptController {
    @Autowired private FeeReceiptService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID parkingSessionId, @RequestBody FeeReceipt fr) {
        String msg = service.saveFeeReceipt(parkingSessionId, fr);
        if (msg.equals("A Fee receipt was saved successfully"))
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        if (msg.equals("All Fee receipts are only for EXITED parking sessions"))
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/motorist/{plateNumber}")
    public ResponseEntity<?> byMotorist(@PathVariable String plateNumber) {
        return new ResponseEntity<>(service.getByMotorist(plateNumber), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = service.deleteFeeReceipt(id);
        if (msg.equals("The Fee receipt was deleted successfully"))
            return new ResponseEntity<>(msg, HttpStatus.OK);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
}