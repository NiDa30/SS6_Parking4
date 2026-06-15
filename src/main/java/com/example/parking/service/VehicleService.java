package com.example.parking.service;

import com.example.parking.dto.PageResponse;
import com.example.parking.dto.VehicleCreateRequest;
import com.example.parking.dto.VehicleResponse;
import com.example.parking.entity.Vehicle;
import com.example.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse createVehicle(VehicleCreateRequest request) {
        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.getLicensePlate())
                .color(request.getColor())
                .type(request.getType())
                .build();
        Vehicle saved = vehicleRepository.save(vehicle);
        return VehicleResponse.builder()
                .id(saved.getId())
                .licensePlate(saved.getLicensePlate())
                .color(saved.getColor())
                .type(saved.getType())
                .build();
    }

    public PageResponse<VehicleResponse> getPagedVehicles(int page, int size, String sortBy, String direction, String keyword) {
        if (page < 0) page = 0;

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.by(sortBy);
            if (direction != null && direction.equalsIgnoreCase("DESC")) {
                sort = sort.descending();
            } else {
                sort = sort.ascending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VehicleResponse> vehiclePage = vehicleRepository.findAllByKeyword(keyword, pageable);

        return PageResponse.<VehicleResponse>builder()
                .items(vehiclePage.getContent())
                .page(vehiclePage.getNumber())
                .size(vehiclePage.getSize())
                .totalItems(vehiclePage.getTotalElements())
                .totalPages(vehiclePage.getTotalPages())
                .isLast(vehiclePage.isLast())
                .build();
    }
}
