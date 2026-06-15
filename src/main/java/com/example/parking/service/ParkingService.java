package com.example.parking.service;

import com.example.parking.dto.TicketRequest;
import com.example.parking.dto.TicketResponse;
import com.example.parking.dto.TicketSummaryResponse;
import com.example.parking.entity.ParkingTicket;
import com.example.parking.entity.Vehicle;
import com.example.parking.entity.Zone;
import com.example.parking.repository.ParkingTicketRepository;
import com.example.parking.repository.VehicleRepository;
import com.example.parking.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ZoneRepository zoneRepository;

    @Transactional(readOnly = true)
    public List<TicketSummaryResponse> getTodayTicketsSummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return ticketRepository.findAllTodayTickets(startOfDay, endOfDay);
    }

    @Transactional
    public TicketResponse checkIn(TicketRequest req) {
        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Zone zone = zoneRepository.findById(req.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        // Check if vehicle is already in the parking lot
        if (ticketRepository.findFirstByVehicleAndCheckOutTimeIsNullOrderByCheckInTimeDesc(vehicle).isPresent()) {
            throw new RuntimeException("Vehicle is already in the parking lot");
        }

        // Check if zone has capacity
        if (zone.getOccupiedSpots() >= zone.getCapacity()) {
            throw new RuntimeException("Zone is full");
        }

        // Create new ticket
        ParkingTicket ticket = ParkingTicket.builder()
                .vehicle(vehicle)
                .zone(zone)
                .checkInTime(LocalDateTime.now())
                .build();

        // Update zone occupied spots
        zone.setOccupiedSpots(zone.getOccupiedSpots() + 1);
        zoneRepository.save(zone);

        ParkingTicket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    @Transactional
    public TicketResponse checkOut(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        ParkingTicket ticket = ticketRepository.findFirstByVehicleAndCheckOutTimeIsNullOrderByCheckInTimeDesc(vehicle)
                .orElseThrow(() -> new RuntimeException("No active ticket found for this vehicle"));

        // Set checkout time
        ticket.setCheckOutTime(LocalDateTime.now());

        // Update zone occupied spots
        Zone zone = ticket.getZone();
        zone.setOccupiedSpots(Math.max(0, zone.getOccupiedSpots() - 1));
        zoneRepository.save(zone);

        ParkingTicket updatedTicket = ticketRepository.save(ticket);

        return mapToResponse(updatedTicket);
    }

    private TicketResponse mapToResponse(ParkingTicket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .licensePlate(ticket.getVehicle().getLicensePlate())
                .zoneName(ticket.getZone().getName())
                .checkInTime(ticket.getCheckInTime())
                .checkOutTime(ticket.getCheckOutTime())
                .build();
    }
}
