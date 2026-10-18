package com.rutaverde.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Actividad {
    private String id;
    private String nombre;
    private String descripcion;
    private int cupoDisponible;
    private LocalDate fecha;
    private Guia guia;
    private List<String> serviciosIncluidos;
    private long precio;

    public Actividad() {
        this.serviciosIncluidos = new ArrayList<>();
    }

    public Actividad(String id, String nombre, String descripcion, int cupoDisponible, LocalDate fecha, Guia guia, List<String> serviciosIncluidos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cupoDisponible = cupoDisponible;
        this.fecha = fecha;
        this.guia = guia;
        this.serviciosIncluidos = (serviciosIncluidos != null) ? serviciosIncluidos : new ArrayList<>();
    }

    public boolean tieneCupo(int cantidad) {
        return cantidad > 0 && this.cupoDisponible >= cantidad;
    }

    public void descontarCupo(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero.");
        }
        if (!tieneCupo(cantidad)) {
            throw new IllegalArgumentException("No hay cupo suficiente para la actividad: " + this.nombre);
        }
        this.cupoDisponible -= cantidad;
    }

    public void agregarServicio(String servicio) {
        if (servicio != null && !servicio.trim().isEmpty()) {
            this.serviciosIncluidos.add(servicio);
        }
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

    public int getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(int cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
    }

    public List<String> getServiciosIncluidos() {
        return serviciosIncluidos;
    }

    public void setServiciosIncluidos(List<String> serviciosIncluidos) {
        this.serviciosIncluidos = serviciosIncluidos;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cupoDisponible=" + cupoDisponible +
                ", fecha=" + fecha +
                ", guia=" + (guia != null ? guia.getNombre() : "Sin guía") +
                ", serviciosIncluidos=" + serviciosIncluidos +
                '}';
    }
}
