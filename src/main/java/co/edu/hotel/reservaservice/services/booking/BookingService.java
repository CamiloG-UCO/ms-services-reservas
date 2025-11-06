package co.edu.hotel.reservaservice.services.booking;

import co.edu.hotel.reservaservice.domain.Booking;
import co.edu.hotel.reservaservice.repository.IBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;

    @Autowired
    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking findByRoomCode(String email, String roomCode) {
        if (email == null || email.isBlank() || roomCode == null || roomCode.isBlank()) {
            throw new IllegalArgumentException("El correo o el código de habitación no pueden estar vacíos");
        }

        Booking booking = bookingRepository.findByRoomCodeAndClientEmail(roomCode, email);
        if (booking == null) {
            throw new IllegalArgumentException("No existe una reserva con el código " + roomCode);
        }

        return booking;
    }

    public void deleteBookingByRoomCode(String email, String roomCode) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("No se puede cancelar la reserva hasta que valide la información de su correo electrónico");
        }

        Booking booking = findByRoomCode(email, roomCode);

        try {
            bookingRepository.delete(booking);
        } catch (RuntimeException e) {
            throw new RuntimeException("No fue posible cancelar la reserva en este momento. Intente más tarde.", e);
        }
    }
}