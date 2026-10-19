package edu.uniquindio.co.rutaverde.model.entities;

public class Cliente {
    private String id;
    private String nombre;
    private String identificacion;
    private String email;
    private String telefono;

    public Cliente() {
    }

    public Cliente(String id, String nombre, String identificacion, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.email = email;
        this.telefono = telefono;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
