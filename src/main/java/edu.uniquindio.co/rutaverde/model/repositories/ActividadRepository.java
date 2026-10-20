package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Actividad;

public class ActividadRepository extends MemoryRepository<Actividad> {
    public ActividadRepository() { super(Actividad::getId); }
}
