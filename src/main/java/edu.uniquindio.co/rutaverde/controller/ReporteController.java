package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.model.services.GeneradorReportes;
import java.time.LocalDate;
import java.util.List;

public class ReporteController {
    private final MainController app;
    public ReporteController(MainController app) { this.app = app; }
    private GeneradorReportes generador() { return new GeneradorReportes(app.reservas.listar()); }
    public long ingresos(LocalDate inicio, LocalDate fin) { return generador().calcularIngresos(inicio, fin); }
    public Producto masVendido() { return generador().obtenerProductoMasVendido(); }
    public int viajeros(Producto producto) { return generador().contarViajerosPorProducto(producto); }
    public List<Producto> productos() { return app.productos.listar(); }
    public long confirmadas() { return app.reservas.listar().stream().filter(r -> "CONFIRMADA".equals(r.getEstado())).count(); }
}
