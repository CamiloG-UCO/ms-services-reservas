package co.edu.hotel.reservaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Bill {
    @Id
    private String id;
    private String code;
    private LocalDateTime date;
    private Double total;
    private String reservationCode;
    private String email;
    private String hotel;
    private String room;
    private String username;
}
