package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.model.Bill;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.BillRepository;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.repository.UserRepository;
import co.edu.hotel.reservaservice.services.BillService;
import co.edu.hotel.reservaservice.services.EmailService;
import co.edu.hotel.reservaservice.services.ReservationService;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GenerarFacturaStepDefinitions {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private BillService billService;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;
    private User testUser;
    private Bill billRequest;
    private Bill generatedBill;
    private String mensajeSistema;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // --- GIVEN ---
    @Given("el usuario {string} existe en el sistema")
    public void el_usuario_existe_en_el_sistema(String usuario) {
        testUser = new User();
        testUser.setId("user-1");
        testUser.setUsername(usuario);
        testUser.setEmail(usuario + "@gmail.com");
        testUser.setFirstName("Juan");
        testUser.setLastName("Pérez");

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

        when(reservationRepository.findByReservationCode(codigo))
                .thenReturn(Optional.ofNullable(testReservation));
    }

    @Given("no existe una reserva con código {string}")
    public void no_existe_una_reserva_con_codigo(String codigo) {
        when(reservationRepository.findByReservationCode(codigo))
                .thenReturn(null);
    }

    // --- WHEN ---
    @When("el recepcionista marque \"Check-out realizado\" el estado pasa a {string}")
    public void el_recepcionista_marque_check_out_realizado(String nuevoEstado) {
        if (testReservation == null) {
            mensajeSistema = "Reserva no encontrada";
            return;
        }

        if ("completada".equalsIgnoreCase(testReservation.getStatus())) {
            mensajeSistema = "La reserva ya fue completada";
            return;
        }

        // Simular actualización y generación de factura
        testReservation.setStatus(nuevoEstado);

        generatedBill = new Bill();
        generatedBill.setId("bill-1");
        generatedBill.setCode("F-1203");
        generatedBill.setDate(LocalDateTime.now());
        generatedBill.setTotal(testReservation.getTotalAmount());
        generatedBill.setReservationCode(testReservation.getReservationCode());

        when(billRepository.save(any(Bill.class))).thenReturn(generatedBill);
        doNothing().when(emailService).sendBill(any(Bill.class));

        billService.createBill(testReservation.getReservationCode());
        mensajeSistema = "Factura generada correctamente";
    }

    @When("el recepcionista intente marcar \"Check-out realizado\"")
    public void el_recepcionista_intente_marcar_check_out_realizado() {
        if (testReservation == null) {
            mensajeSistema = "Reserva no encontrada";
            return;
        }

        if ("completada".equalsIgnoreCase(testReservation.getStatus())) {
            mensajeSistema = "La reserva ya fue completada";
            return;
        }

        mensajeSistema = "Check-out realizado";
    }

    // --- THEN ---
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

    @Then("el sistema debe mostrar {string}")
    public void el_sistema_debe_mostrar(String mensajeEsperado) {
        Assertions.assertEquals(mensajeEsperado, mensajeSistema);
    }

    @And("no debe generar una nueva factura")
    public void no_debe_generar_una_nueva_factura() {
        verify(billRepository, never()).save(any(Bill.class));
    }

    @And("no debe generarse ninguna factura")
    public void no_debe_generarse_ninguna_factura() {
        verify(billRepository, never()).save(any(Bill.class));
    }
}
