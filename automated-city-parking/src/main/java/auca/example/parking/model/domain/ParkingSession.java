package auca.example.parking.model.domain;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_session")
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_date")
    private LocalDate sessionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessionStatus status;

    @Column(name = "bay_label")
    private String bayLabel;

    @Column(name = "hours")
    private int hours;

    @ManyToOne
    @JoinColumn(name = "motorist_id")
    private Motorist motorist;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private Attendant attendant;

    public ParkingSession() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getBayLabel() {
        return bayLabel;
    }

    public void setBayLabel(String bayLabel) {
        this.bayLabel = bayLabel;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Motorist getMotorist() {
        return motorist;
    }

    public void setMotorist(Motorist motorist) {
        this.motorist = motorist;
    }

    public Attendant getAttendant() {
        return attendant;
    }

    public void setAttendant(Attendant attendant) {
        this.attendant = attendant;
    }
}