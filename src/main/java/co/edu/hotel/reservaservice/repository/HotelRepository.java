package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    
    Optional<Hotel> findByName(String name);
}
