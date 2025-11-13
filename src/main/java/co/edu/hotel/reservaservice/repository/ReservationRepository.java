package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    
    Optional<Reservation> findByReservationCode(String reservationCode);
    
    List<Reservation> findByUserId(String userId);
    
    List<Reservation> findByRoomId(String roomId);
    
    @Query("{ 'roomId': ?0, 'status': 'confirmada', " +
           "$or: [ " +
           "{ 'startDate': { $lte: ?2 }, 'endDate': { $gte: ?1 } }, " +
           "{ 'startDate': { $gte: ?1, $lte: ?2 } } " +
           "] }")
    List<Reservation> findConflictingReservations(String roomId, LocalDate startDate, LocalDate endDate);
}
