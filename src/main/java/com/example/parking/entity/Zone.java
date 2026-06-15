package com.example.parking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "occupied_spots")
    private Integer occupiedSpots;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<ParkingTicket> parkingTickets;
}
