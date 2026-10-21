package edu.uniquindio.co.rutaverde.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private List<Actividad> actividades;
    private long precioTotal;

    public Producto() {
        this.actividades = new ArrayList<>();
    }

    public Producto(String id, String nombre, String descripcion, List<Actividad> actividades, long precioTotal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.actividades = (actividades != null) ? actividades : new ArrayList<>();
        this.precioTotal = precioTotal;
    }

    public void agregarActividad(Actividad actividad) {
        if (actividad != null && !this.actividades.contains(actividad)) {
            this.actividades.add(actividad);
        }
    }

    public void removerActividad(Actividad actividad) {
        this.actividades.remove(actividad);
    }

    public long calcularPrecio() {
        if (this.actividades == null || this.actividades.isEmpty()) {
            return this.precioTotal;
        }
        long suma = 0;
        for (Actividad act : this.actividades) {
            suma += act.getPrecio();
        }
        if (suma > 0) {
            this.precioTotal = suma;
        }
        return this.precioTotal;
    }

    public boolean tieneCupo(int cantidad) {
        if (this.actividades == null || this.actividades.isEmpty()) {
            return false;
        }
        for (Actividad act : this.actividades) {
            if (!act.tieneCupo(cantidad)) {
                return false;
            }
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public long getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(long precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", actividades=" + actividades.size() +
                ", precioTotal=" + precioTotal +
                '}';
    }
}
