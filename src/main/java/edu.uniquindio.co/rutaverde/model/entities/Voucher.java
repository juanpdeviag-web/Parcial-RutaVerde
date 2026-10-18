package com.rutaverde.model.entities;

import java.time.LocalDateTime;

public class Voucher {
    private String numero;
    private String tipo;
    private String contenido;
    private LocalDateTime fechaEmision;

    public Voucher() {
        this.fechaEmision = LocalDateTime.now();
    }

    public Voucher(String numero, String tipo, String contenido, LocalDateTime fechaEmision) {
        this.numero = numero;
        this.tipo = tipo;
        this.contenido = contenido;
        this.fechaEmision = fechaEmision;
    }

    public String generarContenido() {
        return String.format("Voucher [%s] - Tipo: %s - Fecha: %s%n%s",
                numero, tipo, fechaEmision, contenido != null ? contenido : "");
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "numero='" + numero + '\'' +
                ", tipo='" + tipo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}
