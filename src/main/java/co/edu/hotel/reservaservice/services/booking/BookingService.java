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

    public void deleteBookingByRoomCode(String email, String roomCode){
        Booking booking = findByRoomCode(email, roomCode);

        bookingRepository.delete(booking);
    }

    public Booking findByRoomCode(String email, String code) {
        if  (this.bookingRepository.findByRoomCodeAndClientEmail(code, email) != null) {
            return this.bookingRepository.findByRoomCodeAndClientEmail(code,  email);
        } else {
            throw new IllegalArgumentException("No existe reserva con el codigo "+code+" que pertenezca al usuario "+email);
        }
    }
}
