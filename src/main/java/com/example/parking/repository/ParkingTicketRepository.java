package com.example.parking.repository;

import com.example.parking.dto.TicketSummaryResponse;
import com.example.parking.entity.ParkingTicket;
import com.example.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {
    Optional<ParkingTicket> findFirstByVehicleAndCheckOutTimeIsNullOrderByCheckInTimeDesc(Vehicle vehicle);

    @Query("SELECT new com.example.parking.dto.TicketSummaryResponse(t.id, v.licensePlate, z.name, t.checkInTime, t.checkOutTime) " +
           "FROM ParkingTicket t " +
           "JOIN t.vehicle v " +
           "JOIN t.zone z " +
           "WHERE t.checkInTime >= :startOfDay AND t.checkInTime <= :endOfDay")
    List<TicketSummaryResponse> findAllTodayTickets(@Param("startOfDay") LocalDateTime startOfDay, 
                                                  @Param("endOfDay") LocalDateTime endOfDay);
}
