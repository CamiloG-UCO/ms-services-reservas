package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends MongoRepository<Booking, String> {

    Booking findByClientEmail(String email);

    @Query("{ 'room.code': ?0, 'client.email': ?1 }")
    List<Booking> findByRoomCodeAndClientEmail(String code, String email);

}
