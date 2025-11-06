package co.edu.hotel.reservaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    private String name;
    private String hotelId;
    private String hotelName;
    private Integer capacity;
    private String status; // "disponible", "ocupada", "mantenimiento"
    private Double pricePerNight;
    private String description;
    private String type; // "Premium vista al mar", "Standard", etc.
}
