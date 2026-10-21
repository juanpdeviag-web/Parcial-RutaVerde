package edu.uniquindio.co.rutaverde.patrones.estructural;

import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.model.services.*;
import edu.uniquindio.co.rutaverde.util.EmailService;
import edu.uniquindio.co.rutaverde.util.LuhnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaFacadeIntegrationTest {

    private Agencia agencia;
    private Cliente cliente;
    private Actividad actividad;
    private Producto producto;
    private ValidadorTarjeta validador;
    private PasarelaPagoAdapter pasarela;
    private PagoAndinoAPI pagoAPI;
    private GestorReservas gestor;
    private GeneradorVoucher generadorVoucher;
    private EmailService emailService;
    private ReservaFacade facade;

    @BeforeEach
    void setUp() {
        agencia = new Agencia(
                "RutaVerde", "900123456", "12345",
                "Dir", "Tel", "email", "web"
        );

        cliente = new Cliente(
                "1", "Juan", "123",
                "juan@email.com", "555-1111"
        );

        actividad = new Actividad(
                "1", "Act1", "Desc", 10,
                LocalDate.now(), null, new ArrayList<>()
        );

        producto = new Producto(
                "1", "Prod1", "Desc",
                new ArrayList<>(List.of(actividad)), 100000
        );

        validador = new ValidadorTarjeta(new LuhnValidator());

        pagoAPI = spy(new PagoAndinoAPI());
        pasarela = new PasarelaPagoAdapter(pagoAPI);

        gestor = new GestorReservas(validador, pasarela);
        generadorVoucher = new GeneradorVoucher(agencia);
        emailService = new EmailService();

        facade = new ReservaFacade(
                validador, pasarela, gestor,
                generadorVoucher, emailService
        );
    }

    @Test
    void testReservaFacadeConfirmacionExitosa() {
        Reserva reserva = facade.confirmarReserva(
                cliente, producto, LocalDate.now(),
                "4539 1488 0343 6467"
        );

        assertNotNull(reserva);
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertEquals(9, actividad.getCupoDisponible());
        assertNotNull(reserva.getVoucher());
        verify(pagoAPI).procesarCobro(eq("4539 1488 0343 6467"), eq(100000L), startsWith("RUTAVERDE-"));
        assertEquals("PA-1001", pasarela.obtenerAutorizacion());
        assertTrue(reserva.getVoucher().getContenido().contains("Pasarela: PagoAndino"));
    }

    @Test
    void testReservaFacadeSinCupo() {
        actividad = new Actividad(
                "1", "Act1", "Desc", 0,
                LocalDate.now(), null, new ArrayList<>()
        );

        producto = new Producto(
                "1", "Prod1", "Desc",
                new ArrayList<>(List.of(actividad)), 100000
        );

        facade = new ReservaFacade(
                validador,
                pasarela,
                gestor,
                generadorVoucher,
                emailService
        );

        Reserva reserva = facade.confirmarReserva(
                cliente, producto, LocalDate.now(),
                "4539 1488 0343 6467"
        );

        assertNull(reserva);
        assertEquals(0, actividad.getCupoDisponible());
        verifyNoInteractions(pagoAPI);
    }

    @Test
    void testReservaFacadeTarjetaInvalida() {
        Reserva reserva = facade.confirmarReserva(
                cliente, producto, LocalDate.now(),
                "1234567890123456"
        );

        assertNull(reserva);
        assertEquals(10, actividad.getCupoDisponible());
        verifyNoInteractions(pagoAPI);
    }

    @Test
    void testRechazoDePagoAndinoNoConfirmaNiDescuentaCupo() {
        producto.setPrecioTotal(400000000L);
        Reserva reserva = facade.confirmarReserva(cliente, producto, LocalDate.now(), "4539148803436467");
        verify(pagoAPI).procesarCobro(eq("4539148803436467"), eq(400000000L), startsWith("RUTAVERDE-"));
        assertNull(reserva);
        assertTrue(gestor.listarReservas().isEmpty());
        assertEquals(10, actividad.getCupoDisponible());
    }

    @Test
    void tarjetaRegistradaManualmenteLlegaSinSustituirseAPagoAndino() {
        var app = new edu.uniquindio.co.rutaverde.controller.MainController();
        var tarjeta = new edu.uniquindio.co.rutaverde.controller.TarjetaController(app)
                .agregar("4111 1111 1111 1111");
        Reserva reserva = facade.confirmarReserva(cliente, producto, LocalDate.now(), tarjeta.getNumero());
        assertNotNull(reserva);
        verify(pagoAPI).procesarCobro(eq("4111111111111111"), eq(100000L), startsWith("RUTAVERDE-"));
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertTrue(reserva.getVoucher().getContenido().contains("PagoAndino"));
    }
}
