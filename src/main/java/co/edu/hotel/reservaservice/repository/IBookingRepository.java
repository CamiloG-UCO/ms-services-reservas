package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends MongoRepository<Booking, String> {

    Booking findByClientEmail(String email);

    Booking findByRoomCodeAndClientEmail(String code, String email);

}
