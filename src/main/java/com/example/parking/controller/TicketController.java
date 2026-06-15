package com.example.parking.controller;

import com.example.parking.dto.ApiResponse;
import com.example.parking.dto.TicketRequest;
import com.example.parking.dto.TicketResponse;
import com.example.parking.dto.TicketSummaryResponse;
import com.example.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final ParkingService parkingService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<TicketSummaryResponse>>> getTodayTicketsSummary() {
        List<TicketSummaryResponse> summary = parkingService.getTodayTicketsSummary();
        return ResponseEntity.ok(ApiResponse.<List<TicketSummaryResponse>>builder()
                .success(true)
                .message("Fetch today's tickets summary successfully")
                .data(summary)
                .build());
    }

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<TicketResponse>> checkIn(@RequestBody TicketRequest request) {
        try {
            TicketResponse response = parkingService.checkIn(request);
            return ResponseEntity.ok(ApiResponse.<TicketResponse>builder()
                    .success(true)
                    .message("Check-in successful")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<TicketResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/check-out/{vehicleId}")
    public ResponseEntity<ApiResponse<TicketResponse>> checkOut(@PathVariable Long vehicleId) {
        try {
            TicketResponse response = parkingService.checkOut(vehicleId);
            return ResponseEntity.ok(ApiResponse.<TicketResponse>builder()
                    .success(true)
                    .message("Check-out successful")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<TicketResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
