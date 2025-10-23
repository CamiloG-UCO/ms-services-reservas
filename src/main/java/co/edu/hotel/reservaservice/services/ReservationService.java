package co.edu.hotel.reservaservice.services;

import co.edu.hotel.reservaservice.dto.ReservationRequest;
import co.edu.hotel.reservaservice.dto.ReservationResponse;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.model.Room;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.repository.RoomRepository;
import co.edu.hotel.reservaservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, String username) {
        log.info("Creating reservation for user: {} and room: {}", username, request.getRoomId());

        // Validate dates
        validateReservationDates(request.getStartDate(), request.getEndDate());

        // Get user information
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // Get room information and validate availability
        Room room = roomRepository.findByIdAndStatus(request.getRoomId(), "disponible")
                .orElseThrow(() -> new RuntimeException("Habitación no disponible o no encontrada"));

        // Check for conflicting reservations
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                request.getRoomId(), request.getStartDate(), request.getEndDate());
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("La habitación no está disponible para las fechas seleccionadas");
        }

        // Calculate total amount
        long nights = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (nights <= 0) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        double totalAmount = nights * room.getPricePerNight();

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setReservationCode(generateReservationCode());
        reservation.setUserId(user.getId());
        reservation.setUsername(user.getUsername());
        reservation.setUserEmail(user.getEmail());
        reservation.setRoomId(room.getId());
        reservation.setRoomName(room.getName());
        reservation.setHotelId(room.getHotelId());
        reservation.setHotelName(room.getHotelName());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setStatus("confirmada");
        reservation.setTotalAmount(totalAmount);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        // Save reservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Send confirmation email asynchronously
        try {
            emailService.sendReservationConfirmation(savedReservation);
        } catch (Exception e) {
            log.error("Error sending confirmation email", e);
        }

        log.info("Reservation created successfully with code: {}", savedReservation.getReservationCode());

        // Return response
        return mapToResponse(savedReservation, "Habitación reservada con éxito, código de reserva " + savedReservation.getReservationCode());
    }

    public List<ReservationResponse> getUserReservations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        return reservations.stream()
                .map(reservation -> mapToResponse(reservation, null))
                .toList();
    }

    public ReservationResponse getReservationByCode(String reservationCode, String username) {
        Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Verify that the reservation belongs to the user
        if (!reservation.getUsername().equals(username)) {
            throw new RuntimeException("No tiene permisos para ver esta reserva");
        }

        return mapToResponse(reservation, null);
    }

    private void validateReservationDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        
        if (startDate.isBefore(today)) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior a hoy");
        }
        
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    private String generateReservationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return "R-" + code;
    }

    private ReservationResponse mapToResponse(Reservation reservation, String message) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setReservationCode(reservation.getReservationCode());
        response.setUsername(reservation.getUsername());
        response.setUserEmail(reservation.getUserEmail());
        response.setRoomName(reservation.getRoomName());
        response.setHotelName(reservation.getHotelName());
        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setStatus(reservation.getStatus());
        response.setTotalAmount(reservation.getTotalAmount());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setMessage(message);
        return response;
    }
}
