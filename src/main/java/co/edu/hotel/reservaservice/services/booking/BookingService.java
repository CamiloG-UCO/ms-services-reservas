package co.edu.hotel.reservaservice.services.booking;

import co.edu.hotel.reservaservice.domain.Booking;
import co.edu.hotel.reservaservice.repository.IBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;

    @Autowired
    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void deleteBookingByRoomCode(String roomCode){
        Booking booking = findByRoomCode(roomCode);

        bookingRepository.delete(booking);
    }

    private Booking findByRoomCode(String code) {
        if  (this.bookingRepository.findByRoomCode(code) != null) {
            return this.bookingRepository.findByRoomCode(code);
        } else {
            throw new IllegalArgumentException("No existe reserva con el codigo "+code);
        }
    }
}
