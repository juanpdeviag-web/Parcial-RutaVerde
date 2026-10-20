package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Reserva;
import edu.uniquindio.co.rutaverde.model.services.GestorReservas;
import java.util.List;
import java.util.Objects;

/** Consulta el almacenamiento del gestor sin duplicar reservas. */
public class ReservaRepository {
    private final GestorReservas gestor;
    public ReservaRepository(GestorReservas gestor) { this.gestor = Objects.requireNonNull(gestor); }
    public List<Reserva> listar() { return gestor.listarReservas(); }
    public Reserva buscar(int numero) { return gestor.buscarReserva(numero); }
}
