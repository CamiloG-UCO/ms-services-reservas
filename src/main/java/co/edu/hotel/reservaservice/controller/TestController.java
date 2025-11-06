package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.domain.*;
import co.edu.hotel.reservaservice.repository.IBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final IBookingRepository bookingRepository;

    @GetMapping("/save")
    public String save() {
        Client client = new Client(UUID.randomUUID().toString(), "Juan Perez", "juan@example.com");
        Hotel hotel = new Hotel(UUID.randomUUID().toString(), "Hotel Paraíso");
        Room room = new Room(UUID.randomUUID().toString(), "A101", hotel);
        Status status = new Status(UUID.randomUUID().toString(), "CONFIRMED");

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setRoom(room);
        booking.setStatus(status);
        booking.setDate(new Date().toString());

        bookingRepository.save(booking);
        return "Reserva guardada correctamente";
    }
}

