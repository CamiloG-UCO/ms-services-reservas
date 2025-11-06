package co.edu.hotel.reservaservice.services;

import co.edu.hotel.reservaservice.model.Bill;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.repository.BillRepository;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillService {

    private final BillRepository billRepository;
    private final EmailService emailService;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Bill createBill(String reservationCode) {
        try {
            Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            Bill bill = new Bill();

            bill.setCode(generateBillCode());
            bill.setDate(LocalDateTime.now());
            bill.setTotal(reservation.getTotalAmount());
            bill.setReservationCode(reservationCode);
            bill.setEmail(reservation.getUserEmail());
            bill.setHotel(reservation.getHotelName());
            bill.setRoom(reservation.getRoomName());
            bill.setUsername(reservation.getUsername());

            Bill savedBill = billRepository.save(bill);


            try {
                emailService.sendBill(savedBill);
            } catch (Exception e) {
                log.error("Error bill email", e);
            }

            return bill;

        } catch (Exception e) {
            throw new RuntimeException("Error creating bill", e);
        }
    }

    private String generateBillCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return "R-" + code;
    }
}
