package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.patrones.creacional.ProductoBuilder;
import java.util.*;
import static edu.uniquindio.co.rutaverde.controller.FormValidation.*;

public class ProductoController {
    private final MainController app;
    public ProductoController(MainController app) { this.app = app; }
    public List<Producto> listar() { return app.productos.listar(); }
    public List<Actividad> actividades() { return app.actividades.listar(); }
    public void guardar(Producto actual, String id, String nombre, String descripcion, List<Actividad> actividades) {
        id = texto(id, "ID"); nombre = texto(nombre, "Nombre");
        if (actual == null && app.productos.buscar(id) != null) throw new IllegalArgumentException("El ID ya existe.");
        if (actual != null && app.productoEnUso(actual))
            throw new IllegalArgumentException("El producto tiene reservas. Crea otro producto para conservar el historial.");
        long total = actividades.stream().mapToLong(Actividad::getPrecio).reduce(0L, Math::addExact);
        ProductoBuilder builder = new ProductoBuilder().conId(actual == null ? id : actual.getId())
                .conNombre(nombre).conDescripcion(descripcion.trim()).conPrecio(total);
        actividades.forEach(builder::agregarActividad);
        Producto nuevo = builder.build();
        if (actual == null) app.productos.guardar(nuevo);
        else {
            actual.setNombre(nuevo.getNombre()); actual.setDescripcion(nuevo.getDescripcion());
            actual.setActividades(new ArrayList<>(nuevo.getActividades())); actual.setPrecioTotal(nuevo.getPrecioTotal());
        }
    }
    public void eliminar(Producto p) {
        if (app.productoEnUso(p)) throw new IllegalArgumentException("El producto tiene reservas y no se puede eliminar.");
        app.productos.eliminar(p);
    }
}
