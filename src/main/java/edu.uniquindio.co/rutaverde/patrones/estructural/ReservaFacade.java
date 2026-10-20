package edu.uniquindio.co.rutaverde.patrones.estructural;

import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.model.services.*;
import edu.uniquindio.co.rutaverde.util.EmailService;
import java.time.LocalDate;import java.util.Objects;

public class ReservaFacade {
 private final ValidadorTarjeta validador;
 private final PasarelaPagoAdapter pasarela;
 private final GestorReservas gestorReservas;
 private final GeneradorVoucher generadorVoucher;
 private final EmailService emailService;

 public ReservaFacade(ValidadorTarjeta validador,PasarelaPagoAdapter pasarela,GestorReservas gestorReservas,GeneradorVoucher generadorVoucher,EmailService emailService){
  this.validador=Objects.requireNonNull(validador);
  this.pasarela=Objects.requireNonNull(pasarela);
  this.gestorReservas=Objects.requireNonNull(gestorReservas);
  this.generadorVoucher=Objects.requireNonNull(generadorVoucher);
  this.emailService=Objects.requireNonNull(emailService);
 }

 public Reserva confirmarReserva(Cliente cliente,Producto producto,LocalDate fechaViaje,String numeroTarjeta){
  Reserva reserva=null;
  boolean cupoDescontado=false;
  try {
   if(cliente==null||producto==null||fechaViaje==null)
    throw new IllegalArgumentException("Cliente, producto y fecha son obligatorios");

   if(!verificarCupo(producto))
    throw new IllegalStateException("No hay cupo disponible en todas las actividades");

   if(!validarTarjeta(numeroTarjeta))
    throw new IllegalArgumentException("La tarjeta no es válida (Luhn/formato)");

   reserva=gestorReservas.crearReserva(cliente,producto,fechaViaje);

   if(!procesarCobro(numeroTarjeta,reserva.getValorPagado()))
    throw new IllegalStateException("La pasarela rechazó el pago");

   descontarCupo(producto);cupoDescontado=true;
   reserva.confirmar();registrarReserva(reserva);
   reserva.setVoucher(emitirVoucher(reserva,"DETALLADO"));
   enviarCorreoConfirmacion(cliente,reserva);
   return reserva;
  } catch(RuntimeException ex){
   if(cupoDescontado && reserva!=null && "CONFIRMADA".equals(reserva.getEstado())) { // revertir inventario si falla una operación posterior
    for(Actividad a:producto.getActividades()) a.setCupoDisponible(a.getCupoDisponible()+1);
    reserva.setEstado("ERROR");
   }
   enviarCorreoError(cliente,ex.getMessage()==null?"Error inesperado":ex.getMessage());return null;
  }
 }

 private boolean verificarCupo(Producto p){
  return p!=null&&p.tieneCupo(1);
 }

 private boolean validarTarjeta(String n){
  return validador.validarLongitud(n)&&validador.validarSoloDigitos(n)&&validador.validar(n);
 }

 private boolean procesarCobro(String n,long valor){
  return valor>=0&&pasarela.procesarPago(n,valor,"RUTAVERDE-"+System.nanoTime())==0;
 }

 private void descontarCupo(Producto p){
  for(Actividad a:p.getActividades())a.descontarCupo(1);
 }

 private void registrarReserva(Reserva r){
  gestorReservas.registrar(r);
 }

 private Voucher emitirVoucher(Reserva r,String tipo){
  return generadorVoucher.generarVoucher(r,tipo);
 }

 private void enviarCorreoConfirmacion(Cliente c,Reserva r){
  emailService.enviarCorreoConfirmacion(c,r);
 }

 private void enviarCorreoError(Cliente c,String motivo){
  if(c!=null)emailService.enviarCorreoError(c,motivo);
 }
}
