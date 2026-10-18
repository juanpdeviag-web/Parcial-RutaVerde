package com.rutaverde.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private int numero;
    private Cliente cliente;
    private Producto producto;
    private LocalDate fechaViaje;
    private LocalDateTime fechaReserva;
    private String estado;
    private long valorPagado;
    private Voucher voucher;

    public Reserva() {
        this.fechaReserva = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public Reserva(int numero, Cliente cliente, Producto producto, LocalDate fechaViaje, LocalDateTime fechaReserva, String estado, long valorPagado, Voucher voucher) {
        this.numero = numero;
        this.cliente = cliente;
        this.producto = producto;
        this.fechaViaje = fechaViaje;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
        this.valorPagado = valorPagado;
        this.voucher = voucher;
    }

    public void confirmar() {
        this.estado = "CONFIRMADA";
    }

    public void cancelar() {
        this.estado = "CANCELADA";
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDate getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(LocalDate fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(long valorPagado) {
        this.valorPagado = valorPagado;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "numero=" + numero +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "N/A") +
                ", producto=" + (producto != null ? producto.getNombre() : "N/A") +
                ", fechaViaje=" + fechaViaje +
                ", fechaReserva=" + fechaReserva +
                ", estado='" + estado + '\'' +
                ", valorPagado=" + valorPagado +
                '}';
    }
}
