package com.example.parking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequest {
    private Long vehicleId;
    private Long zoneId;
}
