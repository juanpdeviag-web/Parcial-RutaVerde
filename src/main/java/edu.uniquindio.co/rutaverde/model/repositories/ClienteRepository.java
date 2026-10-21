package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Cliente;

public class ClienteRepository extends MemoryRepository<Cliente> {
    public ClienteRepository() { super(Cliente::getId); }
}
