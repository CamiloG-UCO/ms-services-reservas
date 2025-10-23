package co.edu.hotel.reservaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String reservationCode;
    private String userId;
    private String username;
    private String userEmail;
    private String roomId;
    private String roomName;
    private String hotelId;
    private String hotelName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // "confirmada", "cancelada", "completada"
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
