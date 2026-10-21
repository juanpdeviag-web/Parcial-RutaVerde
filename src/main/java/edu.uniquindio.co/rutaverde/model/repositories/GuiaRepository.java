package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Guia;

public class GuiaRepository extends MemoryRepository<Guia> {
    public GuiaRepository() { super(Guia::getId); }
}
