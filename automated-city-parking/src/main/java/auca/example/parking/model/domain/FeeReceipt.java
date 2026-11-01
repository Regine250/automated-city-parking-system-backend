package auca.example.parking.model.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "fee_receipt")
public class FeeReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "notes")
    private String notes;

    @Column(name = "amount_rw_f")
    private int amountRwf;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "parking_session_id")
    private ParkingSession parkingSession;

    public FeeReceipt() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAmountRwf() {
        return amountRwf;
    }

    public void setAmountRwf(int amountRwf) {
        this.amountRwf = amountRwf;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public ParkingSession getParkingSession() {
        return parkingSession;
    }

    public void setParkingSession(ParkingSession parkingSession) {
        this.parkingSession = parkingSession;
    }
}