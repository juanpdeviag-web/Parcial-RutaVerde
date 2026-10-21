package edu.uniquindio.co.rutaverde.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Guia {
    private String id;
    private String nombre;
    private String identificacion;
    private List<String> especialidades;

    public Guia() {
        this.especialidades = new ArrayList<>();
    }

    public Guia(String id, String nombre, String identificacion, List<String> especialidades) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.especialidades = (especialidades != null) ? especialidades : new ArrayList<>();
    }

    public void agregarEspecialidad(String especialidad) {
        if (especialidad != null && !especialidad.trim().isEmpty() && !this.especialidades.contains(especialidad)) {
            this.especialidades.add(especialidad);
        }
    }

    public void removerEspecialidad(String especialidad) {
        this.especialidades.remove(especialidad);
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    @Override
    public String toString() {
        return "Guia{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", especialidades=" + especialidades +
                '}';
    }
}
