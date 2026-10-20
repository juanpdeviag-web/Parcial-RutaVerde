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

class StructuralPatternsTest {

    private Reserva reservaMock;
    private Agencia agenciaMock;
    private Cliente clienteMock;
    private Producto productoMock;
    private Actividad actividadMock;
    private ValidadorTarjeta validadorMock;
    private PasarelaPagoAdapter pasarelaMock;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @BeforeEach
    void setUp() {
        agenciaMock = new Agencia(
                "RutaVerde", "900123456", "12345",
                "Dir", "Tel", "email", "web"
        );

        clienteMock = new Cliente(
                "1", "Juan", "123",
                "juan@email.com", "555-1111"
        );

        actividadMock = new Actividad(
                "1", "Act1", "Desc", 10,
                LocalDate.now(), null, new ArrayList<>()
        );

        productoMock = new Producto(
                "1", "Prod1", "Desc",
                new ArrayList<>(List.of(actividadMock)), 100000
        );

        validadorMock = new ValidadorTarjeta(new LuhnValidator());

        pasarelaMock = new PasarelaPagoAdapter(new PagoAndinoAPI());

        GestorReservas gestor = new GestorReservas(
                validadorMock, pasarelaMock
        );

        reservaMock = gestor.crearReserva(
                clienteMock, productoMock, LocalDate.now()
        );

        fechaInicio = LocalDate.now().minusDays(30);
        fechaFin = LocalDate.now().plusDays(30);
    }

    @Test
    void testVoucherDetallado() {
        VoucherStrategy strategy = new VoucherDetallado();

        String voucher = strategy.generarVoucher(
                reservaMock, agenciaMock
        );

        assertTrue(voucher.contains("VOUCHER DETALLADO"));
        assertTrue(voucher.contains("RNT:"));
        assertTrue(voucher.contains("POLÍTICA DE CANCELACIÓN"));
    }

    @Test
    void testVoucherCorto() {
        VoucherStrategy strategy = new VoucherCorto();

        String voucher = strategy.generarVoucher(
                reservaMock, agenciaMock
        );

        assertNotNull(voucher);
        assertTrue(voucher.contains("VOUCHER-"));
    }

    @Test
    void testVoucherMovil() {
        VoucherStrategy strategy = new VoucherMovil();

        String voucher = strategy.generarVoucher(
                reservaMock, agenciaMock
        );

        assertNotNull(voucher);
        assertFalse(voucher.isBlank());
    }

    @Test
    void testGeneradorVoucherDetallado() {
        GeneradorVoucher generador = new GeneradorVoucher(agenciaMock);

        String voucher = generador.generarVoucherDetallado(reservaMock);

        assertNotNull(voucher);
        assertTrue(voucher.contains("VOUCHER DETALLADO"));
    }

    @Test
    void testGeneradorReportesCalcularIngresos() {
        List<Reserva> reservas = new ArrayList<>();

        GeneradorReportes generador = new GeneradorReportes(reservas);

        long ingresos = generador.calcularIngresos(
                fechaInicio, fechaFin
        );

        assertEquals(0, ingresos);
    }

    @Test
    void testGeneradorReportesProductoMasVendido() {
        List<Reserva> reservas = new ArrayList<>();

        GeneradorReportes generador = new GeneradorReportes(reservas);

        Producto producto = generador.obtenerProductoMasVendido();

        assertNull(producto);
    }

    @Test
    void testGestorReservasCrearReserva() {
        GestorReservas gestor = new GestorReservas(
                validadorMock, pasarelaMock
        );

        Reserva reserva = gestor.crearReserva(
                clienteMock, productoMock, LocalDate.now()
        );

        assertNotNull(reserva);
        assertEquals("PENDIENTE", reserva.getEstado());
    }

    @Test
    void testGestorReservasConfirmarReserva() {
        GestorReservas gestor = new GestorReservas(
                validadorMock, pasarelaMock
        );

        Reserva reserva = gestor.crearReserva(
                clienteMock, productoMock, LocalDate.now()
        );

        boolean resultado = gestor.confirmarReserva(
                reserva, "4539 1488 0343 6467"
        );

        assertTrue(resultado);
        assertEquals("CONFIRMADA", reserva.getEstado());
    }

    @Test
    void testEmailServiceEnviarCorreo() {
        EmailService emailService = new EmailService();

        boolean resultado = emailService.enviarCorreo(
                "test@email.com", "Asunto", "Contenido"
        );

        assertTrue(resultado);
    }
}
