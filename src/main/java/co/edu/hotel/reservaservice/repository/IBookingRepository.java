package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends MongoRepository<Booking, String> {

    Booking findByClientEmail(String email);

    Booking findByRoomCode(String code);

}
