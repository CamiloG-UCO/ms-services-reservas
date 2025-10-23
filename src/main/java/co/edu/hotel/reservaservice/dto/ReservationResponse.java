package co.edu.hotel.reservaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private String id;
    private String reservationCode;
    private String username;
    private String userEmail;
    private String roomName;
    private String hotelName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private String message;
}
