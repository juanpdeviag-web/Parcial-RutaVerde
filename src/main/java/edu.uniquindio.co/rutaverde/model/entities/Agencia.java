package edu.uniquindio.co.rutaverde.model.entities;

public class Agencia {
    private String nombreComercial;
    private String NIT;
    private String RNT;
    private String direccion;
    private String telefono;
    private String email;
    private String paginaWeb;

    public Agencia() {
    }

    public Agencia(String nombreComercial, String NIT, String RNT, String direccion, String telefono, String email, String paginaWeb) {
        this.nombreComercial = nombreComercial;
        this.NIT = NIT;
        this.RNT = RNT;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.paginaWeb = paginaWeb;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public String getRNT() {
        return RNT;
    }

    public void setRNT(String RNT) {
        this.RNT = RNT;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    @Override
    public String toString() {
        return "Agencia{" +
                "nombreComercial='" + nombreComercial + '\'' +
                ", NIT='" + NIT + '\'' +
                ", RNT='" + RNT + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", paginaWeb='" + paginaWeb + '\'' +
                '}';
    }
}
