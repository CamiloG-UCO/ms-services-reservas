package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.domain.*;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.services.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rest/booking")
public class bookingController {

    private final BookingService bookingService;
    private final ReservationRepository reservationRepository;

    public bookingController(BookingService bookingService, ReservationRepository reservationRepository) {
        this.bookingService = bookingService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/dummy")
    public ResponseEntity<Booking> getDummy(){
        String randomRoomCode = "R" + (int)(Math.random() * 1000);
        Booking booking = new Booking(UUID.randomUUID().toString(), new Client(UUID.randomUUID().toString(), "test", "Test@email.com"), new Room(UUID.randomUUID().toString(), randomRoomCode, new Hotel(UUID.randomUUID().toString(), "BUG HOTEL")), new Status(UUID.randomUUID().toString(), "Confirmed"), new Date());
        Booking savedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/client/{userEmail}/room/{reservationCode}")
    public ResponseEntity<Reservation> booking(@PathVariable String reservationCode, @PathVariable String userEmail){
        Optional<Reservation> reservation = reservationRepository.findByReservationCode(reservationCode);

        if (reservation.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Reservation res = reservation.get();
        if (!res.getUserEmail().equals(userEmail)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/client/{userEmail}/room/{reservationCode}")
    public ResponseEntity<String> deleteBookingByRoomCode(@PathVariable String userEmail, @PathVariable String reservationCode){
        try {
            Optional<Reservation> reservationOpt = reservationRepository.findByReservationCode(reservationCode);

            if (reservationOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("No existe una reserva con el código " + reservationCode);
            }

            Reservation reservation = reservationOpt.get();

            if (!reservation.getUserEmail().equals(userEmail)) {
                return ResponseEntity.badRequest().body("El correo electrónico no coincide con la reserva");
            }

            if ("cancelada".equalsIgnoreCase(reservation.getStatus())) {
                return ResponseEntity.badRequest().body("La reserva ya está cancelada");
            }

            // Actualizar el estado a cancelada
            reservation.setStatus("cancelada");
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);

            return ResponseEntity.ok("Reserva cancelada con éxito");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error al cancelar la reserva: " + ex.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings(){
        try {
            List<Booking> bookings = bookingService.findAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
