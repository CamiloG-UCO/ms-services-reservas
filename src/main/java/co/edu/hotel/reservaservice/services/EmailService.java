package co.edu.hotel.reservaservice.services;

import co.edu.hotel.reservaservice.model.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@hotel.com}")
    private String fromEmail;

    public void sendReservationConfirmation(Reservation reservation) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reservation.getUserEmail());
            message.setSubject("Confirmación de Reserva - " + reservation.getReservationCode());
            
            String emailBody = buildConfirmationEmailBody(reservation);
            message.setText(emailBody);
            
            mailSender.send(message);
            log.info("Confirmation email sent to: {}", reservation.getUserEmail());
            
        } catch (Exception e) {
            log.error("Error sending confirmation email to: {}", reservation.getUserEmail(), e);
        }
    }

    private String buildConfirmationEmailBody(Reservation reservation) {
        return String.format(
            "Estimado/a %s,\n\n" +
            "Su reserva ha sido confirmada exitosamente.\n\n" +
            "Detalles de la reserva:\n" +
            "- Código de reserva: %s\n" +
            "- Hotel: %s\n" +
            "- Habitación: %s\n" +
            "- Fecha de inicio: %s\n" +
            "- Fecha de fin: %s\n" +
            "- Monto total: $%.2f\n\n" +
            "Gracias por elegirnos.\n\n" +
            "Atentamente,\n" +
            "Equipo de Reservas",
            reservation.getUsername(),
            reservation.getReservationCode(),
            reservation.getHotelName(),
            reservation.getRoomName(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            reservation.getTotalAmount()
        );
    }
}
