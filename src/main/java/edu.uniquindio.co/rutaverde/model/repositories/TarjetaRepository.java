package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Tarjeta;

public class TarjetaRepository extends MemoryRepository<Tarjeta> {
    public TarjetaRepository() { super(Tarjeta::getNumero); }
}
