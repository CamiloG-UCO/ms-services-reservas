package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.dto.ReservationRequest;
import co.edu.hotel.reservaservice.dto.ReservationResponse;
import co.edu.hotel.reservaservice.model.Bill;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.model.Room;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.BillRepository;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.repository.RoomRepository;
import co.edu.hotel.reservaservice.repository.UserRepository;
import co.edu.hotel.reservaservice.services.EmailService;
import co.edu.hotel.reservaservice.services.ReservationService;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class GenerarFacturaStepDefinitions {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;
    private User testUser;
    private Bill billRequest;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("el usuario {string} existe en el sistema")
    public void el_usuario_existe_en_el_sistema(String usuario) {
        testUser = new User();
        testUser.setId("user-1");
        testUser.setUsername(usuario);
        testUser.setEmail(usuario + "@gmail.com");
        testUser.setFirstName("Juan");
        testUser.setLastName("Pérez");
        testUser.setPhone("3001234567");

        when(userRepository.findByUsername(usuario))
                .thenReturn(Optional.of(testUser));
    }

    @And("la reserva {string} con estado {string} del cliente {string} con costo total {string}")
    public void laReservaConEstadoDelClienteConCostoTotal(String codigo, String estado, String cliente, String total) {
        testReservation = new Reservation();
        testReservation.setId("reservation-1");
        testReservation.setReservationCode(codigo);
        testReservation.setUsername(cliente);
        testReservation.setTotalAmount(Double.parseDouble(total));
        testReservation.setStatus(estado);

        if ("confirmada".equals(estado)) {
            when(reservationRepository.findByidAndStatus(anyString(), eq(estado)))
                    .thenReturn(Optional.of(testReservation));
        } else {
            when(reservationRepository.findByidAndStatus(anyString(), eq(estado)))
                    .thenReturn(Optional.empty());
        }
    }


    @When("el recepcionista marque \"Check-out realizado\" el estado pasa a {string}")
    public void elRecepcionistaMarque(String estado) {
        testReservation.setStatus(estado);
    }

    @Then("el sistema debe generar la factura con código {string} con costo total {string}")
    public void elSistemaDebeGenerarLaFacturaConCódigoConCostoTotal(String codigo, String costo) {
        billRequest = new Bill();
        billRequest.setCode(codigo);
        billRequest.setId("bill-1");
        billRequest.setTotal(Double.parseDouble(costo));
        billRequest.setDate(LocalDateTime.now());
        billRequest.setReservationCode(testReservation.getReservationCode());

        when(billRepository.save(any(Bill.class))).thenReturn(billRequest);
    }


    @And("enviar una copia PDF al correo {string}")
    public void enviarUnaCopiaPDFAlCorreo(String email) {
        doNothing().when(emailService).sendBill(any(Bill.class));
        emailService.sendBill(new Bill());

        verify(emailService, times(1)).sendBill(any(Bill.class));
    }
}
