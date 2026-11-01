package auca.example.parking.model.domain;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;



@Entity
@Table(name = "attendant")

public class Attendant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "badge_number")
    private String badgeNumber;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @ManyToMany
    @JoinTable(name = "attendant_vehicle_type",
            joinColumns = @JoinColumn(name = "attendant_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_type_id"))
    private List<VehicleType> vehicleTypes = new ArrayList<>();

    public Attendant(){}

    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getBadgeNumber(){
        return badgeNumber;
    }
    public void setBadgeNumber(String badgeNumber){
        this.badgeNumber = badgeNumber;
    }
    public ParkingLot getParkingLot(){
        return parkingLot;
    }
    public void setParkingLot(ParkingLot parkingLot){
        this.parkingLot = parkingLot;
    }

    public List<VehicleType> getVehicleTypes(){
        return vehicleTypes;
    }
    public void setVehicleTypes(List<VehicleType> vehicleTypes){
        this.vehicleTypes = vehicleTypes;
    }
}
