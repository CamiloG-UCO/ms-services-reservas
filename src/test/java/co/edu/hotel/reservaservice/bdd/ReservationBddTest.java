package co.edu.hotel.reservaservice.bdd;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BDD Tests - Historia de Usuario BE-1.1: Crear una nueva reserva")
public class ReservationBddTest {

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

    @BeforeEach
    void setUp() {
        // Given: habitación "Premium vista al mar" del hotel "Santa Marta Resort" con estado "disponible" y capacidad "2"
        testRoom = new Room();
        testRoom.setId("room-1");
        testRoom.setName("Premium vista al mar");
        testRoom.setHotelId("hotel-1");
        testRoom.setHotelName("Santa Marta Resort");
        testRoom.setCapacity(2);
        testRoom.setStatus("disponible");
        testRoom.setPricePerNight(250000.0);

        // Given: usuario "juan.perez" existe en el sistema
        testUser = new User();
        testUser.setId("user-1");
        testUser.setUsername("juan.perez");
        testUser.setEmail("juan.perez@gmail.com");
        testUser.setFirstName("Juan");
        testUser.setLastName("Pérez");
    }

    @Test
    @DisplayName("Escenario: Registrar reserva en habitación disponible")
    void testRegistrarReservaEnHabitacionDisponible() {
        // Given
        when(userRepository.findByUsername("juan.perez"))
                .thenReturn(Optional.of(testUser));
        when(roomRepository.findByIdAndStatus("room-1", "disponible"))
                .thenReturn(Optional.of(testRoom));
        when(reservationRepository.findConflictingReservations(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        Reservation savedReservation = createMockReservation();
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(savedReservation);

        // When: el usuario "juan.perez" ingrese fecha de inicio "2025-12-12" y fecha final "2025-12-31" y presione "Reservar"
        ReservationRequest request = new ReservationRequest();
        request.setRoomId("room-1");
        request.setStartDate(LocalDate.parse("2025-12-12"));
        request.setEndDate(LocalDate.parse("2025-12-31"));

        long startTime = System.currentTimeMillis();
        ReservationResponse response = reservationService.createReservation(request, "juan.perez");
        long endTime = System.currentTimeMillis();

        // Then: antes de 5 segundos el sistema debe mostrar "Habitación reservada con éxito, código de reserva R-XXXX"
        long executionTime = endTime - startTime;
        assertTrue(executionTime < 5000, "La operación tardó más de 5 segundos: " + executionTime + "ms");

        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertTrue(response.getMessage().contains("Habitación reservada con éxito, código de reserva"));
        assertEquals("R-8791", response.getReservationCode());
        assertEquals("juan.perez", response.getUsername());
        assertEquals("Premium vista al mar", response.getRoomName());
        assertEquals("Santa Marta Resort", response.getHotelName());
        assertEquals("confirmada", response.getStatus());
        assertEquals(4750000.0, response.getTotalAmount());

        // And: enviar un correo de confirmación a "juan.perez@gmail.com"
        verify(emailService, times(1)).sendReservationConfirmation(any(Reservation.class));
        assertEquals("juan.perez@gmail.com", response.getUserEmail());
    }

    @Test
    @DisplayName("Escenario: Intentar reservar habitación no disponible")
    void testIntentarReservarHabitacionNoDisponible() {
        // Given: habitación no disponible
        when(userRepository.findByUsername("juan.perez"))
                .thenReturn(Optional.of(testUser));
        when(roomRepository.findByIdAndStatus("room-1", "disponible"))
                .thenReturn(Optional.empty()); // Habitación no disponible

        // When
        ReservationRequest request = new ReservationRequest();
        request.setRoomId("room-1");
        request.setStartDate(LocalDate.parse("2025-12-12"));
        request.setEndDate(LocalDate.parse("2025-12-31"));

        // Then: el sistema debe mostrar el error "Habitación no disponible o no encontrada"
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(request, "juan.perez");
        });

        assertTrue(exception.getMessage().contains("Habitación no disponible o no encontrada"));
    }

    @Test
    @DisplayName("Escenario: Intentar reservar con fechas inválidas")
    void testIntentarReservarConFechasInvalidas() {
        // When: fecha de inicio en el pasado (no necesitamos mocks para validación de fechas)
        ReservationRequest request = new ReservationRequest();
        request.setRoomId("room-1");
        request.setStartDate(LocalDate.parse("2023-12-12")); // Fecha en el pasado
        request.setEndDate(LocalDate.parse("2025-12-31"));

        // Then: el sistema debe mostrar el error "La fecha de inicio no puede ser anterior a hoy"
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(request, "juan.perez");
        });

        assertTrue(exception.getMessage().contains("La fecha de inicio no puede ser anterior a hoy"));
    }

    private Reservation createMockReservation() {
        Reservation reservation = new Reservation();
        reservation.setId("reservation-1");
        reservation.setReservationCode("R-8791");
        reservation.setUserId(testUser.getId());
        reservation.setUsername(testUser.getUsername());
        reservation.setUserEmail(testUser.getEmail());
        reservation.setRoomId(testRoom.getId());
        reservation.setRoomName(testRoom.getName());
        reservation.setHotelId(testRoom.getHotelId());
        reservation.setHotelName(testRoom.getHotelName());
        reservation.setStartDate(LocalDate.parse("2025-12-12"));
        reservation.setEndDate(LocalDate.parse("2025-12-31"));
        reservation.setStatus("confirmada");
        reservation.setTotalAmount(4750000.0);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservation;
    }
}
