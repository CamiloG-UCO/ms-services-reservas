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

        java.util.List<Booking> bookings = bookingRepository.findByRoomCodeAndClientEmail(roomCode, email);
        if (bookings == null || bookings.isEmpty()) {
            throw new IllegalArgumentException("No existe una reserva con el código " + roomCode);
        }

        return bookings.get(0);
    }

    public void deleteBookingByRoomCode(String email, String roomCode) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("No se puede cancelar la reserva hasta que valide la información de su correo electrónico");
        }

        if (roomCode == null || roomCode.isBlank()) {
            throw new IllegalArgumentException("El código de habitación no puede estar vacío");
        }

        java.util.List<Booking> bookings = bookingRepository.findByRoomCodeAndClientEmail(roomCode, email);

        if (bookings == null || bookings.isEmpty()) {
            throw new IllegalArgumentException("No existe una reserva con el código " + roomCode);
        }

        try {
            for (Booking booking : bookings) {
                if (!booking.isCancelled()) {
                    booking.cancelBooking("Cancelada por el usuario");
                    bookingRepository.save(booking);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("No fue posible cancelar la reserva en este momento. Intente más tarde.", e);
        }
    }


    public java.util.List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking saveBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        return bookingRepository.save(booking);
    }
}