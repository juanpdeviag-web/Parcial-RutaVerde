package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.patrones.creacional.VoucherFactory;
import java.time.LocalDate;
import java.util.List;

public class ReservaController {
    private final MainController app;
    public ReservaController(MainController app) { this.app = app; }
    public List<Reserva> listar() { return app.reservas.listar(); }
    public List<Cliente> clientes() { return app.clientes.listar(); }
    public List<Producto> productos() { return app.productos.listar(); }
    /** Autorización generada por PagoAndino para el último cobro de esta sesión. */
    public String ultimaAutorizacionPago() { return app.pasarela.obtenerAutorizacion(); }
    public Reserva confirmar(Cliente c, Producto p, LocalDate fecha, String tarjeta) {
        if (c == null || p == null || fecha == null)
            throw new IllegalArgumentException("Selecciona cliente, producto y fecha de viaje.");
        if (fecha.isBefore(LocalDate.now())) throw new IllegalArgumentException("La fecha de viaje no puede estar en el pasado.");
        if (!p.tieneCupo(1)) throw new IllegalArgumentException("El producto no tiene cupo en todas sus actividades.");
        if (!app.validador.validar(tarjeta)) throw new IllegalArgumentException("La tarjeta no supera la validación Luhn.");
        Reserva r = app.facade.confirmarReserva(c, p, fecha, tarjeta);
        if (r == null) throw new IllegalArgumentException("No se pudo confirmar la reserva. Revisa cupos y valor del pago.");
        return r;
    }
    public void cancelar(Reserva r) {
        if (r == null || !"CONFIRMADA".equals(r.getEstado()))
            throw new IllegalArgumentException("Selecciona una reserva confirmada.");
        if (!app.gestor.cancelarReserva(r)) throw new IllegalArgumentException("No se pudo cancelar la reserva.");
    }
    public String voucher(Reserva r, String tipo) {
        if (r == null) throw new IllegalArgumentException("Selecciona una reserva.");
        Voucher generado = app.vouchers.generarVoucher(r, tipo);
        Voucher voucher = VoucherFactory.crearVoucher(tipo, r, app.agencia);
        voucher.setContenido(generado.getContenido());
        return voucher.generarContenido();
    }
}
