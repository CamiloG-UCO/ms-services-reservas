package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.domain.*;
import co.edu.hotel.reservaservice.services.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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
        Booking booking = new Booking(UUID.randomUUID().toString(), new Client(UUID.randomUUID().toString(), "test", "Test@email.com"), new Room(UUID.randomUUID().toString(), "R-321", new Hotel(UUID.randomUUID().toString(), "BUG HOTEL")), new Status(UUID.randomUUID().toString(), "Confirmed"), new Date());
        return ResponseEntity.ok(booking);
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
        } catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }

    }
}
