package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.*;
import java.time.LocalDate;
import java.util.*;
import static edu.uniquindio.co.rutaverde.controller.FormValidation.*;

public class ActividadController {
    private final MainController app;
    public ActividadController(MainController app) { this.app = app; }
    public List<Actividad> listar() { return app.actividades.listar(); }
    public List<Guia> guias() { return app.guias.listar(); }
    public void guardar(Actividad actual, String id, String nombre, String descripcion,
                        String cupos, LocalDate fecha, Guia guia, String servicios, String precio) {
        id = texto(id, "ID"); nombre = texto(nombre, "Nombre");
        int cantidad = cupos(cupos); long valor = precio(precio);
        if (fecha == null) throw new IllegalArgumentException("Selecciona una fecha.");
        if (guia == null) throw new IllegalArgumentException("Selecciona un guía; regístralo primero en Guías.");
        if (actual == null && app.actividades.buscar(id) != null) throw new IllegalArgumentException("El ID ya existe.");
        if (actual != null && app.reservas.listar().stream()
                .anyMatch(r -> r.getProducto().getActividades().contains(actual)))
            throw new IllegalArgumentException("Esta actividad pertenece a una reserva. Conserva sus datos y crea otra actividad.");
        Actividad a = actual == null ? new Actividad() : actual;
        if (actual == null) a.setId(id);
        a.setNombre(nombre); a.setDescripcion(descripcion.trim()); a.setCupoDisponible(cantidad);
        a.setFecha(fecha); a.setGuia(guia); a.setServiciosIncluidos(lista(servicios)); a.setPrecio(valor);
        app.actividades.guardar(a);
    }
    public void eliminar(Actividad a) {
        if (app.actividadEnUso(a)) throw new IllegalArgumentException("La actividad forma parte de un producto.");
        app.actividades.eliminar(a);
    }
}
