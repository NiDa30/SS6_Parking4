package com.example.parking.controller;

import com.example.parking.dto.ApiResponse;
import com.example.parking.dto.PageResponse;
import com.example.parking.dto.VehicleCreateRequest;
import com.example.parking.dto.VehicleResponse;
import com.example.parking.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@RequestBody VehicleCreateRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.ok(ApiResponse.<VehicleResponse>builder()
                .success(true)
                .message("Vehicle created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VehicleResponse>>> getVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String keyword) {

        PageResponse<VehicleResponse> response = vehicleService.getPagedVehicles(page, size, sortBy, direction, keyword);
        return ResponseEntity.ok(ApiResponse.<PageResponse<VehicleResponse>>builder()
                .success(true)
                .message("Fetch vehicles successfully")
                .data(response)
                .build());
    }
}
