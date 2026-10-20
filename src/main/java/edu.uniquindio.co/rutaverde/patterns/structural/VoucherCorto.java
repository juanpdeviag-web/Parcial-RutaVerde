package edu.uniquindio.co.rutaverde.patterns.structural;
import edu.uniquindio.co.rutaverde.model.entities.*;

public class VoucherCorto implements VoucherStrategy {
 @Override
 public String generarVoucher(Reserva r, Agencia a) {
  if(r==null) throw new IllegalArgumentException("La reserva es obligatoria");
  return "VOUCHER-"+r.getNumero()+" | "+(r.getProducto()==null?"N/A":r.getProducto().getNombre())+" | "+r.getFechaViaje();
 }
}
