package edu.uniquindio.co.rutaverde.controller;
import edu.uniquindio.co.rutaverde.model.entities.Cliente;
import java.util.List;
import static edu.uniquindio.co.rutaverde.controller.FormValidation.*;

public class ClienteController {
    private final MainController app;
    public ClienteController(MainController app) { this.app = app; }
    public List<Cliente> listar() { return app.clientes.listar(); }
    public void guardar(Cliente actual, String id, String nombre, String documento, String correo, String telefono) {
        id = texto(id, "ID"); nombre = texto(nombre, "Nombre"); documento = texto(documento, "Identificación");
        correo = email(correo); telefono = texto(telefono, "Teléfono");
        if (actual == null && app.clientes.buscar(id) != null) throw new IllegalArgumentException("El ID ya existe.");
        for (Cliente c : listar()) if (c != actual && documento.equals(c.getIdentificacion()))
            throw new IllegalArgumentException("La identificación ya está registrada.");
        Cliente c = actual == null ? new Cliente() : actual;
        if (actual == null) c.setId(id);
        c.setNombre(nombre); c.setIdentificacion(documento); c.setEmail(correo); c.setTelefono(telefono);
        app.clientes.guardar(c);
    }
    public void eliminar(Cliente c) {
        if (app.clienteEnUso(c)) throw new IllegalArgumentException("El cliente tiene reservas y no se puede eliminar.");
        app.clientes.eliminar(c);
    }
}
