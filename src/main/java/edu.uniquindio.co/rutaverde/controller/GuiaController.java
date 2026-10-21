package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.Guia;
import java.util.List;
import static edu.uniquindio.co.rutaverde.controller.FormValidation.*;

public class GuiaController {
    private final MainController app;
    public GuiaController(MainController app) { this.app = app; }
    public List<Guia> listar() { return app.guias.listar(); }
    public void guardar(Guia actual, String id, String nombre, String documento, String especialidades) {
        id = texto(id, "ID"); nombre = texto(nombre, "Nombre"); documento = texto(documento, "Identificación");
        if (actual == null && app.guias.buscar(id) != null) throw new IllegalArgumentException("El ID ya existe.");
        for (Guia g : listar()) if (g != actual && documento.equals(g.getIdentificacion()))
            throw new IllegalArgumentException("La identificación ya está registrada.");
        Guia g = actual == null ? new Guia() : actual;
        if (actual == null) g.setId(id);
        g.setNombre(nombre); g.setIdentificacion(documento); g.setEspecialidades(lista(especialidades));
        app.guias.guardar(g);
    }
    public void eliminar(Guia g) {
        if (app.guiaEnUso(g)) throw new IllegalArgumentException("El guía está asignado a una actividad.");
        app.guias.eliminar(g);
    }
}
