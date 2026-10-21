package edu.uniquindio.co.rutaverde.model.repositories;

import edu.uniquindio.co.rutaverde.model.entities.Producto;

public class ProductoRepository extends MemoryRepository<Producto> {
    public ProductoRepository() { super(Producto::getId); }
}
