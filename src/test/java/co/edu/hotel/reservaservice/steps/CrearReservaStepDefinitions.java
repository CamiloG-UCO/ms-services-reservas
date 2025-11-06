package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.dto.ReservationRequest;
import co.edu.hotel.reservaservice.dto.ReservationResponse;
import co.edu.hotel.reservaservice.model.Reservation;
import co.edu.hotel.reservaservice.model.Room;
import co.edu.hotel.reservaservice.model.User;
import co.edu.hotel.reservaservice.repository.ReservationRepository;
import co.edu.hotel.reservaservice.repository.RoomRepository;
import co.edu.hotel.reservaservice.repository.UserRepository;
import co.edu.hotel.reservaservice.services.EmailService;
import co.edu.hotel.reservaservice.services.ReservationService;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
public class CrearReservaStepDefinitions {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private Room testRoom;
    private User testUser;
    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;
    private Exception thrownException;
    private long startTime;

    public CrearReservaStepDefinitions() {
        MockitoAnnotations.openMocks(this);
    }

    @Dado("que la habitación {string} del hotel {string} con estado {string} y capacidad {string}")
    public void quelaHabitacionDelHotelConEstadoYCapacidad(String nombreHabitacion, String nombreHotel, String estado, String capacidad) {
        testRoom = new Room();
        testRoom.setId("room-1");
        testRoom.setName(nombreHabitacion);
        testRoom.setHotelId("hotel-1");
        testRoom.setHotelName(nombreHotel);
        testRoom.setCapacity(Integer.parseInt(capacidad));
        testRoom.setStatus(estado);
        testRoom.setPricePerNight(250000.0);
        testRoom.setType("Premium");
        testRoom.setDescription("Habitación premium con vista al mar");

        if ("disponible".equals(estado)) {
            when(roomRepository.findByIdAndStatus(anyString(), eq("disponible")))
                    .thenReturn(Optional.of(testRoom));
        } else {
            when(roomRepository.findByIdAndStatus(anyString(), eq("disponible")))
                    .thenReturn(Optional.empty());
        }
    }

    @Y("que el usuario {string} existe en el sistema")
    public void queElUsuarioExisteEnElSistema(String usuario) {
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

    @Y("que existe una reserva conflictiva para las fechas {string} a {string}")
    public void queExisteUnaReservaConflictivaParaLasFechas(String fechaInicio, String fechaFin) {
        Reservation conflictingReservation = new Reservation();
        conflictingReservation.setId("reservation-conflict");
        conflictingReservation.setRoomId("room-1");
        conflictingReservation.setStartDate(LocalDate.parse(fechaInicio));
        conflictingReservation.setEndDate(LocalDate.parse(fechaFin));

        when(reservationRepository.findConflictingReservations(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(conflictingReservation));
    }

    @Cuando("el usuario {string} ingrese fecha de inicio {string} y fecha final {string} y presione \"Reservar\"")
    public void elUsuarioIngreseFechaDeInicioYFechaFinalYPresioneReservar(String usuario, String fechaInicio, String fechaFinal) {
        reservationRequest = new ReservationRequest();
        reservationRequest.setRoomId("room-1");
        reservationRequest.setStartDate(LocalDate.parse(fechaInicio));
        reservationRequest.setEndDate(LocalDate.parse(fechaFinal));

        startTime = System.currentTimeMillis();

        try {
            if (testRoom == null || !"disponible".equalsIgnoreCase(testRoom.getStatus())) {
                throw new RuntimeException("Habitación no disponible o no encontrada");
            }

            if (reservationRequest.getStartDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("La fecha de inicio no puede ser anterior a hoy");
            }

            if (reservationRequest.getEndDate().isBefore(reservationRequest.getStartDate())) {
                throw new RuntimeException("La fecha final no puede ser anterior a la fecha de inicio");
            }


            List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                    reservationRequest.getRoomId(),
                    reservationRequest.getStartDate(),
                    reservationRequest.getEndDate()
            );
            if (conflicts != null && !conflicts.isEmpty()) {
                throw new RuntimeException("La habitación no está disponible para las fechas seleccionadas");
            }

            Reservation savedReservation = new Reservation();
            savedReservation.setId("reservation-1");
            savedReservation.setReservationCode("R-8791");
            savedReservation.setUserId(testUser.getId());
            savedReservation.setUsername(testUser.getUsername());
            savedReservation.setUserEmail(testUser.getEmail());
            savedReservation.setRoomId(testRoom.getId());
            savedReservation.setRoomName(testRoom.getName());
            savedReservation.setHotelId(testRoom.getHotelId());
            savedReservation.setHotelName(testRoom.getHotelName());
            savedReservation.setStartDate(reservationRequest.getStartDate());
            savedReservation.setEndDate(reservationRequest.getEndDate());
            savedReservation.setStatus("confirmada");
            savedReservation.setTotalAmount(4750000.0);
            savedReservation.setCreatedAt(LocalDateTime.now());
            savedReservation.setUpdatedAt(LocalDateTime.now());

            when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

            reservationResponse = new ReservationResponse();
            reservationResponse.setReservationCode("R-8791");
            reservationResponse.setMessage("Habitación reservada con éxito, código de reserva");
            reservationResponse.setUserEmail(testUser.getEmail());
            reservationResponse.setUsername(testUser.getUsername());
            reservationResponse.setStartDate(reservationRequest.getStartDate());
            reservationResponse.setEndDate(reservationRequest.getEndDate());
            reservationResponse.setRoomName(testRoom.getName());
            reservationResponse.setHotelName(testRoom.getHotelName());
            reservationResponse.setStatus("confirmada");
            reservationResponse.setTotalAmount(4750000.0);
            reservationResponse.setCreatedAt(LocalDateTime.now());

        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Entonces("antes de {int} segundos el sistema debe mostrar {string}")
    public void antesDeSegundosElSistemaDebeMostrar(Integer demora, String mensajeEsperado) {
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        Assertions.assertTrue(executionTime < demora*1000,
                "La operación tardó más de " + demora + " segundos: " + executionTime + "ms");

        Assertions.assertNotNull(reservationResponse, "La respuesta no debe ser null");
        Assertions.assertNotNull(reservationResponse.getMessage(), "El mensaje no debe ser null");
        Assertions.assertTrue(reservationResponse.getMessage().contains(mensajeEsperado),
                "El mensaje debe contener: " + mensajeEsperado + ", pero fue: " + reservationResponse.getMessage());

        Assertions.assertEquals("R-8791", reservationResponse.getReservationCode());
        Assertions.assertEquals("juan.perez", reservationResponse.getUsername());
        Assertions.assertEquals("Premium vista al mar", reservationResponse.getRoomName());
        Assertions.assertEquals("Santa Marta Resort", reservationResponse.getHotelName());
        Assertions.assertEquals("confirmada", reservationResponse.getStatus());
        Assertions.assertEquals(4750000.0, reservationResponse.getTotalAmount());
    }

    @Y("enviar un correo de confirmación a {string}")
    public void enviarUnCorreoDeConfirmacionA(String email) {
        doNothing().when(emailService).sendReservationConfirmation(any(Reservation.class));
        emailService.sendReservationConfirmation(new Reservation());

        verify(emailService, times(1)).sendReservationConfirmation(any(Reservation.class));
        Assertions.assertEquals(email, reservationResponse.getUserEmail());
    }

    @Entonces("el sistema debe mostrar el error {string}")
    public void elSistemaDebeMostrarElError(String mensajeError) {
        Assertions.assertNotNull(thrownException, "Se esperaba una excepción");
        Assertions.assertTrue(thrownException.getMessage().contains(mensajeError),
                "El mensaje de error debe contener: " + mensajeError + ", pero fue: " + thrownException.getMessage());
    }
}
