package edu.uniquindio.co.rutaverde.controller;

import edu.uniquindio.co.rutaverde.model.entities.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DemoIntegrationTest {
    @Test
    void demoCreaCatalogosRelacionadosYCobraDiezReservasMediantePagoAndino() {
        MainController app = new MainController();
        app.cargarEjemplo();
        assertTrue(app.demoCargado());
        assertEquals(10, app.clientes.listar().size());
        assertEquals(10, app.guias.listar().size());
        assertEquals(10, app.actividades.listar().size());
        assertEquals(10, app.productos.listar().size());
        assertEquals(10, app.reservas.listar().size());
        assertEquals("PA-1010", app.pasarela.obtenerAutorizacion());
        for (Reserva r : app.reservas.listar()) {
            assertEquals("CONFIRMADA", r.getEstado());
            assertSame(app.clientes.buscar(r.getCliente().getId()), r.getCliente());
            assertSame(app.productos.buscar(r.getProducto().getId()), r.getProducto());
            assertEquals(r.getProducto().calcularPrecio(), r.getValorPagado());
            assertNotNull(r.getVoucher());
            assertTrue(r.getVoucher().getContenido().contains("Pasarela: PagoAndino"));
        }
        for (Producto p : app.productos.listar()) {
            assertEquals(p.getActividades().stream().mapToLong(Actividad::getPrecio).sum(), p.calcularPrecio());
            p.getActividades().forEach(a -> assertSame(app.actividades.buscar(a.getId()), a));
        }
        assertEquals(8, app.actividades.buscar("A001").getCupoDisponible());
        assertEquals(10, app.actividades.buscar("A003").getCupoDisponible());
        assertFalse(app.productos.buscar("P009").tieneCupo(1));
        ReporteController reportes = new ReporteController(app);
        assertEquals(1540000L, reportes.ingresos(LocalDate.now(), LocalDate.now()));
        assertEquals(10, reportes.confirmadas());
        assertEquals("P001", reportes.masVendido().getId());
        assertEquals(3, reportes.viajeros(app.productos.buscar("P001")));
    }

    @Test
    void repetirDemoConservaEdicionesEliminacionesYNoRepiteCobros() {
        MainController app = new MainController();
        app.cargarEjemplo();
        Cliente c = app.clientes.buscar("C009");
        new ClienteController(app).guardar(c, c.getId(), "Nombre editado", c.getIdentificacion(), c.getEmail(), c.getTelefono());
        new ClienteController(app).eliminar(app.clientes.buscar("C010"));
        new GuiaController(app).eliminar(app.guias.buscar("G010"));
        new ActividadController(app).eliminar(app.actividades.buscar("A010"));
        new ProductoController(app).eliminar(app.productos.buscar("P010"));
        new ReservaController(app).cancelar(app.reservas.buscar(1));
        int cupos = app.actividades.buscar("A001").getCupoDisponible();
        for (int i = 0; i < 3; i++) app.cargarEjemplo();
        assertEquals(9, app.clientes.listar().size());
        assertEquals(9, app.guias.listar().size());
        assertEquals(9, app.actividades.listar().size());
        assertEquals(9, app.productos.listar().size());
        assertEquals(10, app.reservas.listar().size());
        assertSame(c, app.clientes.buscar("C009"));
        assertEquals("Nombre editado", c.getNombre());
        assertNull(app.clientes.buscar("C010"));
        assertEquals("CANCELADA", app.reservas.buscar(1).getEstado());
        assertEquals(cupos, app.actividades.buscar("A001").getCupoDisponible());
        assertEquals("PA-1010", app.pasarela.obtenerAutorizacion());
    }

    @Test
    void demoNoSobrescribeUnaSesionConDatosManuales() {
        MainController app = new MainController();
        new ClienteController(app).guardar(null, "C001", "Cliente propio", "123", "propio@example.com", "3001234567");
        assertThrows(IllegalArgumentException.class, app::cargarEjemplo);
        assertFalse(app.demoCargado());
        assertEquals(1, app.clientes.listar().size());
        assertEquals("Cliente propio", app.clientes.buscar("C001").getNombre());
        assertTrue(app.reservas.listar().isEmpty());
        assertEquals("PA-1000", app.pasarela.obtenerAutorizacion());
    }

    @Test
    void sePuedeReservarDespuesDelDemoYActualizarReportesYVouchers() {
        MainController app = new MainController();
        app.cargarEjemplo();
        ReservaController controller = new ReservaController(app);
        Reserva nueva = controller.confirmar(app.clientes.buscar("C009"), app.productos.buscar("P001"),
                app.actividades.buscar("A001").getFecha(), "4539148803436467");
        assertEquals(11, nueva.getNumero());
        assertEquals("PA-1011", controller.ultimaAutorizacionPago());
        assertEquals(7, app.actividades.buscar("A001").getCupoDisponible());
        for (String tipo : List.of("DETALLADO", "CORTO", "MOVIL"))
            assertTrue(controller.voucher(nueva, tipo).contains("PagoAndino"));
        assertEquals(1660000L, new ReporteController(app).ingresos(LocalDate.now(), LocalDate.now()));
    }
}
