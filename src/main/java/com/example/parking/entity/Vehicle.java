package com.example.parking.entity;

import com.example.parking.model.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<ParkingTicket> parkingTickets;
}
