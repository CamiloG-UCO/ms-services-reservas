package co.edu.hotel.reservaservice.steps;

import co.edu.hotel.reservaservice.domain.Booking;
import co.edu.hotel.reservaservice.domain.Client;
import co.edu.hotel.reservaservice.domain.Hotel;
import co.edu.hotel.reservaservice.domain.Room;
import co.edu.hotel.reservaservice.domain.Status;
import co.edu.hotel.reservaservice.repository.IBookingRepository;
import co.edu.hotel.reservaservice.services.booking.BookingService;
import io.cucumber.java.es.*;
import org.mockito.Mockito;

import java.util.*;
import static org.junit.Assert.*;

public class CancelarReservaStepDefinitions {

    private IBookingRepository bookingRepository;
    private BookingService bookingService;
    private Booking reservaExistente;
    private Exception error;
    private String mensajeSistema;
    private String emailUsuario;
    private boolean correoEnviado;

    // --- GIVEN ---

    @Dado("existe una reserva con código {string} para el usuario {string} con estado {string}")
    public void existe_una_reserva_con_codigo_para_el_usuario_con_estado(String codigo, String usuario, String estado) {
        bookingRepository = Mockito.mock(IBookingRepository.class);
        bookingService = new BookingService(bookingRepository);

        reservaExistente = new Booking(UUID.randomUUID().toString(),
                new Client(UUID.randomUUID().toString(), usuario, usuario + "@gmail.com"),
                new Room(UUID.randomUUID().toString(), codigo, new Hotel(UUID.randomUUID().toString(), "Hotel Central")),
                new Status(UUID.randomUUID().toString(), estado),
                new Date());

        Mockito.when(bookingRepository.findByRoomCodeAndClientEmail(codigo, usuario + "@gmail.com"))
                .thenReturn(reservaExistente);

        emailUsuario = usuario + "@gmail.com";
        correoEnviado = false;
        error = null;
    }

    @Dado("no existe una reserva con código {string} para el usuario {string}")
    public void no_existe_una_reserva_con_codigo_para_el_usuario(String codigo, String usuario) {
        bookingRepository = Mockito.mock(IBookingRepository.class);
        bookingService = new BookingService(bookingRepository);

        Mockito.when(bookingRepository.findByRoomCodeAndClientEmail(codigo, usuario + "@gmail.com"))
                .thenReturn(null);

        emailUsuario = usuario + "@gmail.com";
        reservaExistente = null;
        correoEnviado = false;
        error = null;
    }

    @Dado("el correo del usuario es {string}")
    public void el_correo_del_usuario_es(String correo) {
        this.emailUsuario = correo;
    }

    @Dado("el servicio de base de datos presenta un error temporal")
    public void el_servicio_de_base_de_datos_presenta_un_error_temporal() {
        if (reservaExistente != null) {
            Mockito.doThrow(new RuntimeException("Error temporal en base de datos"))
                    .when(bookingRepository)
                    .delete(reservaExistente);
        }
    }

    @Dado("el correo del usuario no encontrado")
    public void el_correo_del_usuario_no_encontrado() {
        this.emailUsuario = null;
    }

    // --- WHEN ---

    @Cuando("el usuario presione el botón {string}")
    public void el_usuario_presione_el_boton(String boton) {
        try {
            if (emailUsuario == null) {
                mensajeSistema = "No se puede cancelar la reserva hasta que valide la información de su correo electrónico";
                return;
            }

            Booking reserva = bookingService.findByRoomCode(emailUsuario, reservaExistente != null
                    ? reservaExistente.getRoom().getCode()
                    : "N/A");

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
        try {
            Mockito.verify(bookingRepository).delete(reservaExistente);
        } catch (Exception e) {
            fail("La reserva no fue eliminada: " + e.getMessage());
        }
    }

    @Entonces("mostrar el mensaje {string}")
    public void mostrar_el_mensaje(String mensajeEsperado) {
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

    @Entonces("no debe eliminar la reserva")
    public void no_debe_eliminar_la_reserva() {
        Mockito.verify(bookingRepository, Mockito.never()).delete(Mockito.any());
    }
}