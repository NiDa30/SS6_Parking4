package com.example.parking.dto;

import com.example.parking.model.VehicleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
    private Long id;
    private String licensePlate;
    private String color;
    private VehicleType type;
}
