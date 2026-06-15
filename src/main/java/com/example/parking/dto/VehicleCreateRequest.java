package com.example.parking.dto;

import com.example.parking.model.VehicleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreateRequest {
    private String licensePlate;
    private String color;
    private VehicleType type;
}
