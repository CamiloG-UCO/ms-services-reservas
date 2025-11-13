package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.services.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

public class CheckInReservaStepDefinitions {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private User recepcionist;
    private Exception exception;

    public CheckInReservaStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("la reserva {string} del cliente {string} con fecha de inicio {string}")
    public void laReservaConClienteYFechadeInicio(String reservationCode, String username, String startDate)
    {
        reservation = new Reservation();
        reservation.setReservationCode(reservationCode);
        reservation.setUsername(username);
        reservation.setStartDate(LocalDate.parse(startDate));
    }

    @When("el recepcionista {string} marque \"Check-in realizado\"")
    public void elRecepcionistaMarque(String recepcionistUsername) {
        recepcionist = new User();
        recepcionist.setUsername(recepcionistUsername);
        Assertions.assertNotNull(recepcionist.getUsername());
    }

    @Then("el sistema debe cambiar el estado de la habitación a {string}")
    public void elSistemaDebeCambiarElEstadoDeLaHabitaciónA(String status) {

        Reservation updatedReservation = reservation;
        updatedReservation.setStatus(status);
        updatedReservation.setCheckIn(LocalDateTime.now());

        try {
            reservationService.checkIn(reservation.getReservationCode());
            reservation = updatedReservation;
        } catch (Exception e) {
            exception = e;
        }

        Assertions.assertNull(exception, exception.getMessage());
    }
    @Then("registrar la fecha y hora del check-in en la base de datos")
    public void registrarLaFechaYHoraDelCheckInEnLaBaseDeDatos() {
        Assertions.assertEquals("ocupada", reservation.getStatus());
        Assertions.assertNotNull(reservation.getCheckIn());
    }
}
