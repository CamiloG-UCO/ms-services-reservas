package co.edu.hotel.reservaservice.repository;

import co.edu.hotel.reservaservice.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends MongoRepository<Bill, String> {
}
