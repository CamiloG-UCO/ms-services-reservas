package co.edu.hotel.reservaservice.config;

import co.edu.hotel.reservaservice.model.Hotel;
import co.edu.hotel.reservaservice.model.Room;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.HotelRepository;
import co.edu.hotel.reservaservice.repository.RoomRepository;
import co.edu.hotel.reservaservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeTestData();
    }

    private void initializeTestData() {
        try {
            // Create test hotel if it doesn't exist
            if (hotelRepository.findByName("Santa Marta Resort").isEmpty()) {
                Hotel hotel = new Hotel();
                hotel.setName("Santa Marta Resort");
                hotel.setAddress("Carrera 1 # 20-29");
                hotel.setCity("Santa Marta");
                hotel.setCountry("Colombia");
                hotel.setDescription("Resort frente al mar en Santa Marta");
                hotel = hotelRepository.save(hotel);
                log.info("Created test hotel: {}", hotel.getName());

                // Create test room
                Room room = new Room();
                room.setName("Premium vista al mar");
                room.setHotelId(hotel.getId());
                room.setHotelName(hotel.getName());
                room.setCapacity(2);
                room.setStatus("disponible");
                room.setPricePerNight(250000.0);
                room.setDescription("Habitación premium con vista al mar");
                room.setType("Premium vista al mar");
                roomRepository.save(room);
                log.info("Created test room: {}", room.getName());
            }

            // Create test user if it doesn't exist
            if (userRepository.findByUsername("juan.perez").isEmpty()) {
                User user = new User();
                user.setUsername("juan.perez");
                user.setEmail("juan.perez@gmail.com");
                user.setFirstName("Juan");
                user.setLastName("Pérez");
                user.setPhone("+57 300 123 4567");
                userRepository.save(user);
                log.info("Created test user: {}", user.getUsername());
            }

            log.info("Test data initialization completed");
        } catch (Exception e) {
            log.error("Error initializing test data", e);
        }
    }
}
