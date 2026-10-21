package edu.uniquindio.co.rutaverde.util;
import edu.uniquindio.co.rutaverde.model.entities.*;

public class EmailService {
 public boolean enviarCorreo(String destinatario,String asunto,String contenido){
     if(destinatario==null||destinatario.isBlank())
         return false;
     System.out.println("[EMAIL SIMULADO] Para: "+destinatario+" | Asunto: "+asunto+"\n"+contenido);
     return true;
 }

 public boolean enviarCorreoConfirmacion(Cliente c,Reserva r){
     if(c==null||r==null)
         return false;
     String voucher=r.getVoucher()==null?"Voucher no disponible":r.getVoucher().generarContenido();
     return enviarCorreo(c.getEmail(),"Confirmación de reserva #"+r.getNumero(),"Hola "+c.getNombre()+", tu reserva está confirmada.\n"+voucher);
 }
 public boolean enviarCorreoError(Cliente c,String motivo){
     if(c==null)return false;
     return enviarCorreo(c.getEmail(),"Error en la reserva", "No fue posible completar la reserva: "+motivo);
 }
}
