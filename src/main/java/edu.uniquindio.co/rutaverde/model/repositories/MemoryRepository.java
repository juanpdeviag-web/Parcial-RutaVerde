package edu.uniquindio.co.rutaverde.model.repositories;

import java.util.*;
import java.util.function.Function;

/** Almacenamiento en memoria durante la sesión. */
public class MemoryRepository<T> {
    private final List<T> datos = new ArrayList<>();
    private final Function<T, String> identificador;
    protected MemoryRepository(Function<T, String> identificador) { this.identificador = identificador; }
    public List<T> listar() { return new ArrayList<>(datos); }
    public T buscar(String id) {
        return datos.stream().filter(d -> Objects.equals(identificador.apply(d), id)).findFirst().orElse(null);
    }
    public void guardar(T dato) {
        Objects.requireNonNull(dato);
        String id = identificador.apply(dato);
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El ID es obligatorio.");
        T existente = buscar(id);
        if (existente != null && existente != dato) throw new IllegalArgumentException("Ya existe un registro con ese ID.");
        if (!datos.contains(dato)) datos.add(dato);
    }
    public void eliminar(T dato) { datos.remove(dato); }
}
