package edu.uniquindio.co.rutaverde.controller;

import edu.uniquindio.co.rutaverde.model.entities.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InterfazIntegrationTest {
    @Test
    void flujoCompletoComparteReservasCuposVouchersYReportes() {
        MainController app = new MainController();
        ClienteController clientes = new ClienteController(app);
        GuiaController guias = new GuiaController(app);
        ActividadController actividades = new ActividadController(app);
        ProductoController productos = new ProductoController(app);
        ReservaController reservas = new ReservaController(app);
        // Se crea antes de la venta para verificar que no conserve una copia obsoleta.
        ReporteController reportes = new ReporteController(app);
        clientes.guardar(null, "C1", "Ana", "123", "ana@example.com", "3001234567");
        guias.guardar(null, "G1", "Luis", "456", "Senderismo, Aves");
        LocalDate viaje = LocalDate.now().plusDays(7);
        actividades.guardar(null, "A1", "Bosque", "Recorrido", "8", viaje,
                app.guias.buscar("G1"), "Seguro, Guianza", "85000");
        productos.guardar(null, "P1", "Escapada", "Paquete", actividades.listar());
        Producto producto = app.productos.buscar("P1");
        assertEquals("PA-1000", reservas.ultimaAutorizacionPago());
        Reserva r = reservas.confirmar(app.clientes.buscar("C1"), producto, viaje, "4539 1488 0343 6467");
        assertEquals("PA-1001", reservas.ultimaAutorizacionPago());
        assertSame(r, app.reservas.buscar(r.getNumero()));
        assertEquals("CONFIRMADA", r.getEstado());
        assertEquals(7, app.actividades.buscar("A1").getCupoDisponible());
        assertEquals(85000, reportes.ingresos(LocalDate.now(), LocalDate.now()));
        assertSame(producto, reportes.masVendido());
        assertEquals(1, reportes.viajeros(producto));
        assertTrue(reservas.voucher(r, "DETALLADO").contains("Bosque"));
        assertTrue(reservas.voucher(r, "CORTO").contains("VOUCHER-1"));
        assertTrue(reservas.voucher(r, "MOVIL").contains("\"reserva\":1"));
        for (String tipo : List.of("DETALLADO", "CORTO", "MOVIL"))
            assertTrue(reservas.voucher(r, tipo).contains("PagoAndino"));
        assertTrue(r.getVoucher().getContenido().contains("Pasarela: PagoAndino"));
        reservas.cancelar(r);
        assertEquals("CANCELADA", app.reservas.buscar(r.getNumero()).getEstado());
        assertEquals(0, reportes.ingresos(LocalDate.now(), LocalDate.now()));
        assertEquals(0, reportes.viajeros(producto));
    }

    @Test
    void validacionFallidaNoModificaClienteExistente() {
        MainController app = new MainController();
        cargarCatalogoBasico(app);
        ClienteController controller = new ClienteController(app);
        Cliente c = app.clientes.buscar("C001");
        assertThrows(IllegalArgumentException.class, () ->
                controller.guardar(c, c.getId(), "Otro nombre", c.getIdentificacion(), "incorrecto", "123"));
        assertEquals("María López", c.getNombre());
        assertEquals("maria@example.com", c.getEmail());
        assertThrows(IllegalArgumentException.class, () ->
                controller.guardar(null, "C001", "Otro", "999", "otro@example.com", "123"));
        assertEquals(1, controller.listar().size());
    }

    @Test
    void relacionesImpidenEliminarOAlterarDatosDeUnaReserva() {
        MainController app = new MainController();
        cargarCatalogoBasico(app);
        Cliente c = app.clientes.buscar("C001");
        Guia g = app.guias.buscar("G001");
        Actividad a = app.actividades.buscar("A001");
        Producto p = app.productos.buscar("P001");
        assertThrows(IllegalArgumentException.class, () -> new GuiaController(app).eliminar(g));
        assertThrows(IllegalArgumentException.class, () -> new ActividadController(app).eliminar(a));
        new ReservaController(app).confirmar(c, p, a.getFecha(), "4539148803436467");
        assertThrows(IllegalArgumentException.class, () -> new ClienteController(app).eliminar(c));
        assertThrows(IllegalArgumentException.class, () -> new ProductoController(app).eliminar(p));
        assertThrows(IllegalArgumentException.class, () ->
                new ProductoController(app).guardar(p, p.getId(), "Cambio", "Cambio", List.of(a)));
        assertThrows(IllegalArgumentException.class, () ->
                new ActividadController(app).guardar(a, a.getId(), "Cambio", "Cambio", "99",
                        a.getFecha(), g, "Seguro", "1"));
        assertEquals("Escapada verde", p.getNombre());
        assertEquals(11, a.getCupoDisponible());
    }

    @Test
    void tarjetaInvalidaYFaltaDeCupoNoRegistranVentas() {
        MainController app = new MainController();
        cargarCatalogoBasico(app);
        ReservaController controller = new ReservaController(app);
        Cliente c = app.clientes.buscar("C001");
        Producto p = app.productos.buscar("P001");
        Actividad a = app.actividades.buscar("A001");
        assertThrows(IllegalArgumentException.class, () -> controller.confirmar(c, p, a.getFecha(), "123"));
        assertEquals(12, a.getCupoDisponible());
        a.setCupoDisponible(0);
        assertThrows(IllegalArgumentException.class, () ->
                controller.confirmar(c, p, a.getFecha(), "4539148803436467"));
        assertTrue(controller.listar().isEmpty());
        assertEquals(0, new ReporteController(app).confirmadas());
    }

    @Test
    void catalogosSePuedenEditarYEliminarSinRomperIdentidad() {
        MainController app = new MainController();
        cargarCatalogoBasico(app);
        Producto p = app.productos.buscar("P001");
        Actividad a = app.actividades.buscar("A001");
        new ProductoController(app).guardar(p, p.getId(), "Nuevo paquete", "Descripción", List.of(a));
        assertSame(p, app.productos.buscar("P001"));
        new ActividadController(app).guardar(a, a.getId(), "Nueva actividad", "Descripción", "10",
                a.getFecha(), a.getGuia(), "Seguro", "150000");
        assertEquals(150000, p.calcularPrecio());
        new ProductoController(app).eliminar(p);
        new ActividadController(app).eliminar(a);
        new GuiaController(app).eliminar(app.guias.buscar("G001"));
        new ClienteController(app).eliminar(app.clientes.buscar("C001"));
        assertTrue(app.clientes.listar().isEmpty());
        assertTrue(app.guias.listar().isEmpty());
        assertTrue(app.actividades.listar().isEmpty());
        assertTrue(app.productos.listar().isEmpty());
    }

    // Catálogo mínimo independiente del conjunto demo, que ahora incluye ventas.
    private static void cargarCatalogoBasico(MainController app) {
        new ClienteController(app).guardar(null, "C001", "María López", "1094001001", "maria@example.com", "3001234567");
        new GuiaController(app).guardar(null, "G001", "Andrés García", "1094002002", "Senderismo");
        new ActividadController(app).guardar(null, "A001", "Sendero del bosque", "Recorrido", "12",
                LocalDate.now().plusDays(7), app.guias.buscar("G001"), "Guianza, Seguro", "120000");
        new ProductoController(app).guardar(null, "P001", "Escapada verde", "Experiencia",
                List.of(app.actividades.buscar("A001")));
    }
}
