package edu.uniquindio.co.rutaverde.patrones.estructural;

import edu.uniquindio.co.rutaverde.model.entities.*;
import java.util.stream.Collectors;

public class VoucherDetallado implements VoucherStrategy {
    @Override
    public String generarVoucher(Reserva reserva, Agencia agencia) {
        if (reserva == null || agencia == null) throw new IllegalArgumentException("Reserva y agencia son obligatorias");
        StringBuilder s = new StringBuilder("=== VOUCHER DETALLADO ===\n")
            .append(valor(agencia.getNombreComercial())).append("\nRNT: ").append(valor(agencia.getRNT()))
            .append("\n\nReserva #: ").append(reserva.getNumero())
            .append("\nCliente: ").append(reserva.getCliente()==null?"N/A":valor(reserva.getCliente().getNombre()))
            .append("\nProducto: ").append(reserva.getProducto()==null?"N/A":valor(reserva.getProducto().getNombre()))
            .append("\nFecha viaje: ").append(reserva.getFechaViaje()).append("\n\nACTIVIDADES:");
        if (reserva.getProducto()!=null && reserva.getProducto().getActividades()!=null) {
            for (Actividad a : reserva.getProducto().getActividades()) {
                s.append("\n- ").append(valor(a.getNombre())).append(" | Guía: ")
                 .append(a.getGuia()==null?"Sin asignar":valor(a.getGuia().getNombre())).append(" | Servicios: ")
                 .append(a.getServiciosIncluidos()==null?"Ninguno":a.getServiciosIncluidos().stream().collect(Collectors.joining(", ")));
            }
        }
        return s.append("\n\nPOLÍTICA DE CANCELACIÓN:\nSin costo hasta 48 horas antes del viaje.").toString();
    }
    private String valor(String s) { return s==null?"N/A":s; }
}
