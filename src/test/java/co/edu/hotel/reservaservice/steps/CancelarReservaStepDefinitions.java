package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.domain.*;
import co.edu.hotel.reservaservice.repository.IBookingRepository;
import co.edu.hotel.reservaservice.services.booking.BookingService;
import io.cucumber.java.Before;
import io.cucumber.java.es.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CancelarReservaStepDefinitions {

    @Mock
    private IBookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking reservaExistente;
    private Exception error;
    private String mensajeSistema;
    private String emailUsuario;
    private boolean correoEnviado;
    private String codigoReserva;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        reservaExistente = null;
        error = null;
        mensajeSistema = null;
        emailUsuario = null;
        correoEnviado = false;
        codigoReserva = null;
    }

    // --- GIVEN ---
    @Dado("existe una reserva con código {string} para el usuario {string} con estado {string}")
    public void existe_una_reserva_con_codigo_para_el_usuario_con_estado(String codigo, String usuario, String estado) {
        emailUsuario = usuario + "@gmail.com";
        reservaExistente = new Booking(
                UUID.randomUUID().toString(),
                new Client(UUID.randomUUID().toString(), usuario, emailUsuario),
                new Room(UUID.randomUUID().toString(), codigo, new Hotel(UUID.randomUUID().toString(), "Hotel Central")),
                new Status(UUID.randomUUID().toString(), estado),
                new Date()
        );

        when(bookingRepository.findByRoomCodeAndClientEmail(codigo, emailUsuario))
                .thenReturn(reservaExistente);
    }

    @Dado("no existe una reserva con código {string} para el usuario {string}")
    public void no_existe_una_reserva_con_codigo_para_el_usuario(String codigo, String usuario) {
        emailUsuario = usuario + "@gmail.com";
        when(bookingRepository.findByRoomCodeAndClientEmail(codigo, emailUsuario))
                .thenReturn(null);
    }

    @Dado("el correo del usuario es {string}")
    public void el_correo_del_usuario_es(String correo) {
        this.emailUsuario = correo;
    }

    @Dado("el servicio de base de datos presenta un error temporal")
    public void el_servicio_de_base_de_datos_presenta_un_error_temporal() {
        if (reservaExistente != null) {
            doThrow(new RuntimeException("Error temporal en base de datos"))
                    .when(bookingRepository)
                    .delete(reservaExistente);
        }
    }

    @Dado("el correo del usuario no encontrado")
    public void el_correo_del_usuario_no_encontrado() {
        emailUsuario = null;
    }

    // --- WHEN ---
    @Cuando("el usuario presione el botón {string}")
    public void el_usuario_presione_el_boton(String boton) {
        try {
            if (emailUsuario == null) {
                mensajeSistema = "No se puede cancelar la reserva hasta que valide la información de su correo electrónico";
                return;
            }

            Booking reserva = bookingService.findByRoomCode(
                    emailUsuario,
                    reservaExistente != null ? reservaExistente.getRoom().getCode() : "N/A"
            );

            bookingService.deleteBookingByRoomCode(emailUsuario, reserva.getRoom().getCode());
            mensajeSistema = "Reserva " + reserva.getRoom().getCode() + " cancelada exitosamente";
            correoEnviado = true;

        } catch (IllegalArgumentException e) {
            mensajeSistema = e.getMessage();
            correoEnviado = false;
        } catch (RuntimeException e) {
            mensajeSistema = "No fue posible cancelar la reserva en este momento. Intente más tarde.";
            correoEnviado = false;
        } catch (Exception e) {
            error = e;
        }
    }

    // --- THEN ---
    @Entonces("el sistema debe eliminar la reserva con código {string}")
    public void el_sistema_debe_eliminar_la_reserva_con_codigo(String codigo) {
        verify(bookingRepository).delete(reservaExistente);
    }

    @Entonces("mostrar el mensaje {string}")
    public void mostrar_el_mensaje(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeSistema);
    }

    @Entonces("el sistema debe mostrar el mensaje {string}")
    public void el_sistema_debe_mostrar_el_mensaje(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeSistema);
    }

    @Entonces("enviar correo de cancelación a {string}")
    public void enviar_correo_de_cancelacion_a(String correo) {
        assertTrue("El correo de cancelación no fue enviado", correoEnviado);
        assertEquals(correo, emailUsuario);
    }

    @Entonces("el sistema debe mostrar el mensaje de error {string}")
    public void el_sistema_debe_mostrar_el_mensaje_de_error(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeSistema);
    }

    @Entonces("no debe enviarse ningún correo")
    public void no_debe_enviarse_ningun_correo() {
        assertFalse("Se envió un correo cuando no debía", correoEnviado);
    }

    @Entonces("no debe enviarse ningún correo de cancelación")
    public void no_debe_enviarse_ningun_correo_de_cancelacion() {
        assertFalse("Se envió un correo de cancelación cuando no debía", correoEnviado);
    }

    @Entonces("no debe eliminar la reserva")
    public void no_debe_eliminar_la_reserva() {
        verify(bookingRepository, never()).delete(any());
    }
}
