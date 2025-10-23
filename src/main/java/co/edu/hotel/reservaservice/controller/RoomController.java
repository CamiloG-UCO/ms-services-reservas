package co.edu.hotel.reservaservice.controller;

import co.edu.hotel.reservaservice.model.Room;
import co.edu.hotel.reservaservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomRepository roomRepository;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        try {
            List<Room> rooms = roomRepository.findAll();
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            log.error("Error getting rooms: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        try {
            List<Room> availableRooms = roomRepository.findByStatus("disponible");
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            log.error("Error getting available rooms: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        try {
            return roomRepository.findById(id)
                    .map(room -> ResponseEntity.ok(room))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error getting room by id: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable String hotelId) {
        try {
            List<Room> rooms = roomRepository.findByHotelId(hotelId);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            log.error("Error getting rooms by hotel: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
