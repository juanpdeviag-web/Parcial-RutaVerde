package edu.uniquindio.co.rutaverde.controller;

import edu.uniquindio.co.rutaverde.model.entities.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TarjetaIntegrationTest {
    private static final String MANUAL = "4111111111111111";

    @Test
    void permiteGuardarUnaTarjetaPropiaNormalizadaSinPerderLaDemo() {
        MainController app = new MainController();
        TarjetaController tarjetas = new TarjetaController(app);
        Tarjeta propia = tarjetas.agregar("4111 1111 1111 1111");
        assertEquals(MANUAL, propia.getNumero());
        assertSame(propia, app.tarjetas.buscar(MANUAL));
        assertEquals(2, tarjetas.listar().size());
        assertNotNull(app.tarjetas.buscar(TarjetaController.NUMERO_DEMO));
        assertTrue(tarjetas.etiqueta(propia).endsWith("1111"));
        assertFalse(propia.toString().contains(MANUAL));
        assertEquals("PA-1000", app.pasarela.obtenerAutorizacion());
        assertTrue(new TarjetaController(app).listar().contains(propia));
    }

    @Test
    void rechazaNumerosInvalidosYDuplicadosSinAlterarElCatalogo() {
        MainController app = new MainController();
        TarjetaController tarjetas = new TarjetaController(app);
        assertThrows(IllegalArgumentException.class, () -> tarjetas.agregar(null));
        for (String numero : new String[]{"", "123", "4111111111111112", "4111-1111-1111-1111", "abc"})
            assertThrows(IllegalArgumentException.class, () -> tarjetas.agregar(numero));
        assertEquals(1, tarjetas.listar().size());
        tarjetas.agregar(MANUAL);
        assertThrows(IllegalArgumentException.class, () -> tarjetas.agregar("4111 1111 1111 1111"));
        assertThrows(IllegalArgumentException.class, () -> tarjetas.agregar("4539 1488 0343 6467"));
        assertEquals(2, tarjetas.listar().size());
        assertEquals("PA-1000", app.pasarela.obtenerAutorizacion());
    }

    @Test
    void cargarDemoDespuesDeRegistrarNoBorraNiDuplicaTarjetas() {
        MainController app = new MainController();
        Tarjeta propia = new TarjetaController(app).agregar(MANUAL);
        app.cargarEjemplo();
        app.cargarEjemplo();
        assertEquals(2, app.tarjetas.listar().size());
        assertSame(propia, app.tarjetas.buscar(MANUAL));
        assertEquals(10, app.reservas.listar().size());
        assertEquals("PA-1010", app.pasarela.obtenerAutorizacion());
    }

    @Test
    void tarjetaPropiaYDemoPaganPorElMismoFlujoDePagoAndino() {
        MainController app = new MainController();
        Tarjeta propia = new TarjetaController(app).agregar(MANUAL);
        app.cargarEjemplo();
        ReservaController controller = new ReservaController(app);
        Cliente cliente = app.clientes.buscar("C009");
        Producto producto = app.productos.buscar("P001");
        LocalDate fecha = producto.getActividades().getFirst().getFecha();
        Reserva manual = controller.confirmar(cliente, producto, fecha, propia.getNumero());
        assertEquals("PA-1011", controller.ultimaAutorizacionPago());
        Reserva demo = controller.confirmar(cliente, producto, fecha,
                app.tarjetas.buscar(TarjetaController.NUMERO_DEMO).getNumero());
        assertEquals("PA-1012", controller.ultimaAutorizacionPago());
        assertEquals("CONFIRMADA", manual.getEstado());
        assertEquals("CONFIRMADA", demo.getEstado());
        assertTrue(manual.getVoucher().getContenido().contains("PagoAndino"));
        assertTrue(demo.getVoucher().getContenido().contains("PagoAndino"));
        assertEquals(6, producto.getActividades().getFirst().getCupoDisponible());
        assertEquals(1780000L, new ReporteController(app).ingresos(LocalDate.now(), LocalDate.now()));
        assertEquals(2, app.tarjetas.listar().size());
    }

    @Test
    void rechazoDePagoAndinoConTarjetaPropiaNoConfirmaReservaNiDescuentaCupos() {
        MainController app = new MainController();
        Tarjeta propia = new TarjetaController(app).agregar(MANUAL);
        app.cargarEjemplo();
        Producto producto = app.productos.buscar("P007");
        Actividad actividad = producto.getActividades().getFirst();
        actividad.setPrecio(400000000L);
        int cupos = actividad.getCupoDisponible();
        assertThrows(IllegalArgumentException.class, () -> new ReservaController(app)
                .confirmar(app.clientes.buscar("C009"), producto, actividad.getFecha(), propia.getNumero()));
        assertEquals("PA-1011", app.pasarela.obtenerAutorizacion());
        assertEquals(10, app.reservas.listar().size());
        assertEquals(cupos, actividad.getCupoDisponible());
        assertEquals(2, app.tarjetas.listar().size());
    }
}
