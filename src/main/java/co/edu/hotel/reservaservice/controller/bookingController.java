package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.domain.*;
import co.edu.hotel.reservaservice.services.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rest/booking")
public class bookingController {

    private final BookingService bookingService;

    public bookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/dummy")
    public ResponseEntity<Booking> getDummy(){
        String randomRoomCode = "R" + (int)(Math.random() * 1000);
        Booking booking = new Booking(UUID.randomUUID().toString(), new Client(UUID.randomUUID().toString(), "test", "Test@email.com"), new Room(UUID.randomUUID().toString(), randomRoomCode, new Hotel(UUID.randomUUID().toString(), "BUG HOTEL")), new Status(UUID.randomUUID().toString(), "Confirmed"), new Date());
        Booking savedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/client/{UserEmail}/room/{RoomCode}")
    public ResponseEntity<Booking> booking(@PathVariable String RoomCode, @PathVariable String UserEmail){
        return  ResponseEntity.ok(bookingService.findByRoomCode(UserEmail, RoomCode));
    }

    @DeleteMapping("/client/{UserEmail}/room/{RoomCode}")
    public ResponseEntity<String> deleteBookingByRoomCode(@PathVariable String UserEmail, @PathVariable String RoomCode){
        try {
            bookingService.deleteBookingByRoomCode(UserEmail, RoomCode);
            return ResponseEntity.ok("Reserva eliminada con exito");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error al eliminar la reserva: " + ex.getMessage());
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
