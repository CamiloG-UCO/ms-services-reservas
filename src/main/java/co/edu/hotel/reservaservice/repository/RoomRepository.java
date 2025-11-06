package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    
    List<Room> findByHotelId(String hotelId);
    
    List<Room> findByStatus(String status);
    
    Optional<Room> findByIdAndStatus(String id, String status);
    
    List<Room> findByHotelIdAndStatus(String hotelId, String status);
}
