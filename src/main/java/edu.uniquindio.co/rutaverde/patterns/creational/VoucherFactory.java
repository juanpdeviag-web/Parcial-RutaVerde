package edu.uniquindio.co.rutaverde.patterns.creational;

import edu.uniquindio.co.rutaverde.model.entities.Agencia;
import edu.uniquindio.co.rutaverde.model.entities.Reserva;
import edu.uniquindio.co.rutaverde.model.entities.Voucher;

public class VoucherFactory {
    public static Voucher crearVoucher(String tipo, Reserva reserva, Agencia agencia) {
        if (!"DETALLADO".equals(tipo) && !"CORTO".equals(tipo) && !"MOVIL".equals(tipo)) {
            throw new IllegalArgumentException("Tipo de voucher no válido: " + tipo);
        }
        
        Voucher voucher = new Voucher();
        voucher.setTipo(tipo);
        
        if (reserva != null) {
            voucher.setNumero("V-" + reserva.getNumero());
        }
        
        return voucher;
    }
}
