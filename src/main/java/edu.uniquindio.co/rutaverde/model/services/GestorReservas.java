package edu.uniquindio.co.rutaverde.model.services;

import edu.uniquindio.co.rutaverde.model.entities.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GestorReservas {
 private final ValidadorTarjeta validador; private final PasarelaPagoAdapter pasarela;
 private final List<Reserva> reservas=new ArrayList<>(); private int consecutivo=1;

 public GestorReservas(ValidadorTarjeta validador,PasarelaPagoAdapter pasarela){
  this.validador=Objects.requireNonNull(validador);
  this.pasarela=Objects.requireNonNull(pasarela);
 }

 public synchronized Reserva crearReserva(Cliente c,Producto p,LocalDate fecha){
  if(c==null||p==null||fecha==null)throw new IllegalArgumentException("Cliente, producto y fecha son obligatorios");
  Reserva r=new Reserva();
  r.setNumero(consecutivo++);
  r.setCliente(c);
  r.setProducto(p);
  r.setFechaViaje(fecha);
  r.setFechaReserva(LocalDateTime.now());
  r.setValorPagado(p.calcularPrecio());
  r.setEstado("PENDIENTE");
  return r;
 }

 public boolean confirmarReserva(Reserva r,String tarjeta){
  if(r==null||!validador.validar(tarjeta)||!validador.validarLongitud(tarjeta)||!validador.validarSoloDigitos(tarjeta))
   return false;
  int codigo=pasarela.procesarPago(tarjeta,r.getValorPagado(),"RES-"+r.getNumero());if(codigo!=0)
   return false;
  r.confirmar();
  registrar(r);
  return true;
 }

 public synchronized void registrar(Reserva r){
  if(r!=null&&!reservas.contains(r))
   reservas.add(r);
 }

 public boolean cancelarReserva(Reserva r){
  if(r==null||!reservas.contains(r)||"CANCELADA".equals(r.getEstado()))
   return false;
  r.cancelar();
  return true;
 }

 public Reserva buscarReserva(int n){
  return reservas.stream().filter(r->r.getNumero()==n).findFirst().orElse(null);
 }

 public List<Reserva> listarReservas(){
  return new ArrayList<>(reservas);
 }

 public List<Reserva> listarReservasPorCliente(Cliente c){
  return reservas.stream().filter(r->Objects.equals(r.getCliente(),c)).collect(Collectors.toList());
 }
}
