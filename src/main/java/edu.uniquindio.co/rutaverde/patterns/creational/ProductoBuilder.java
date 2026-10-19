package edu.uniquindio.co.rutaverde.patterns.creational;

import edu.uniquindio.co.rutaverde.model.entities.Actividad;
import edu.uniquindio.co.rutaverde.model.entities.Producto;

public class ProductoBuilder {
    private Producto producto;

    public ProductoBuilder() {
        this.producto = new Producto();
    }

    public ProductoBuilder conId(String id) {
        this.producto.setId(id);
        return this;
    }

    public ProductoBuilder conNombre(String nombre) {
        this.producto.setNombre(nombre);
        return this;
    }

    public ProductoBuilder conDescripcion(String descripcion) {
        this.producto.setDescripcion(descripcion);
        return this;
    }

    public ProductoBuilder agregarActividad(Actividad actividad) {
        this.producto.agregarActividad(actividad);
        return this;
    }

    public ProductoBuilder conPrecio(long precio) {
        this.producto.setPrecioTotal(precio);
        return this;
    }

    public Producto build() {
        if (this.producto.getNombre() == null || this.producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (this.producto.getActividades() == null || this.producto.getActividades().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener al menos una actividad");
        }
        if (this.producto.getPrecioTotal() <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        return this.producto;
    }
}
