package edu.uniquindio.co.rutaverde.model.services;

import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.patterns.structural.*;
import java.time.LocalDateTime;

public class GeneradorVoucher {
 private final Agencia agencia;

 public GeneradorVoucher(Agencia agencia){
     this.agencia=agencia;
 }
 public Voucher generarVoucher(Reserva reserva,String tipo){
  if(reserva==null) throw new IllegalArgumentException("La reserva es obligatoria");
  String normal=tipo==null?"":tipo.trim().toUpperCase();
  VoucherStrategy strategy=switch(normal){case "DETALLADO"->new VoucherDetallado();case "CORTO"->new VoucherCorto();case "MOVIL"->new VoucherMovil();default->throw new IllegalArgumentException("Tipo de voucher no válido: "+tipo);};
  return new Voucher("V-"+reserva.getNumero(),normal,strategy.generarVoucher(reserva,agencia),LocalDateTime.now());
 }
 public String generarVoucherDetallado(Reserva r){
     return new VoucherDetallado().generarVoucher(r,agencia);
 }

 public String generarVoucherCorto(Reserva r){
     return new VoucherCorto().generarVoucher(r,agencia);
 }
 public String generarVoucherMovil(Reserva r){
     return new VoucherMovil().generarVoucher(r,agencia);
 }
}
