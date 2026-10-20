package edu.uniquindio.co.rutaverde.patrones.estructural;

import edu.uniquindio.co.rutaverde.model.entities.Agencia;
import edu.uniquindio.co.rutaverde.model.entities.Reserva;

public interface VoucherStrategy {
    String generarVoucher(Reserva reserva, Agencia agencia);
}
