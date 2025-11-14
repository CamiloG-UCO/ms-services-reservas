package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.dto.ReservationRequest;
import co.edu.hotel.reservaservice.dto.ReservationResponse;
import co.edu.hotel.reservaservice.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {
        
        try {
            String username = authentication.getName();
            log.info("Creating reservation for user: {}", username);
            
            ReservationResponse response = reservationService.createReservation(request, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error creating reservation: {}", e.getMessage());
            ReservationResponse errorResponse = new ReservationResponse();
            errorResponse.setMessage("Error al crear la reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(Authentication authentication) {
        try {
            String username = authentication.getName();
            log.info("Getting reservations for user: {}", username);
            
            List<ReservationResponse> reservations = reservationService.getUserReservations(username);
            return ResponseEntity.ok(reservations);
            
        } catch (Exception e) {
            log.error("Error getting user reservations: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{reservationCode}")
    public ResponseEntity<ReservationResponse> getReservationByCode(
            @PathVariable String reservationCode,
            Authentication authentication) {
        
        try {
            String username = authentication.getName();
            log.info("Getting reservation {} for user: {}", reservationCode, username);
            
            ReservationResponse reservation = reservationService.getReservationByCode(reservationCode, username);
            return ResponseEntity.ok(reservation);
            
        } catch (Exception e) {
            log.error("Error getting reservation by code: {}", e.getMessage());
            ReservationResponse errorResponse = new ReservationResponse();
            errorResponse.setMessage("Error al obtener la reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
