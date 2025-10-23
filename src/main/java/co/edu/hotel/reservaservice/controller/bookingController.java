package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.services.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rest/booking")
public class bookingController {

    private final BookingService bookingService;

    public bookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @DeleteMapping("/delete/{RoomCode}")
    public ResponseEntity<String> deleteBookingByRoomCode(@PathVariable String RoomCode){
        bookingService.deleteBookingByRoomCode(RoomCode);
        return ResponseEntity.ok("Reserva eliminada con exito");
    }

    @DeleteMapping("/delete/{UserEmail}")
    public ResponseEntity<String> deleteBookingByUserEmail(@PathVariable String UserEmail){
        bookingService.deleteBookingByRoomCode(UserEmail);
        return ResponseEntity.ok("Reserva eliminada con exito");
    }
}
