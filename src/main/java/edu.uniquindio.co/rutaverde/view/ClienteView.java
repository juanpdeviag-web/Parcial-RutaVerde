package edu.uniquindio.co.rutaverde.view;
import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.Cliente;
import javafx.scene.control.TextField;

public class ClienteView extends CrudView<Cliente> {
    private final ClienteController controller;
    private final TextField id = new TextField(), nombre = new TextField(), documento = new TextField(),
            correo = new TextField(), telefono = new TextField();
    public ClienteView(MainController app) {
        controller = new ClienteController(app);
        column("ID", Cliente::getId); column("Nombre", Cliente::getNombre);
        column("Identificación", Cliente::getIdentificacion); column("Correo", Cliente::getEmail);
        column("Teléfono", Cliente::getTelefono);
        field("ID *", id); field("Nombre completo *", nombre); field("Identificación *", documento);
        field("Correo electrónico *", correo); field("Teléfono *", telefono);
        correo.setPromptText("nombre@correo.com");
        init("Clientes", "Personas que viajarán con RutaVerde. Selecciona una fila para editar.", controller::listar, controller::eliminar);
    }
    protected void fill(Cliente c) {
        id.setText(c == null ? "" : c.getId()); id.setDisable(c != null);
        nombre.setText(c == null ? "" : c.getNombre()); documento.setText(c == null ? "" : c.getIdentificacion());
        correo.setText(c == null ? "" : c.getEmail()); telefono.setText(c == null ? "" : c.getTelefono());
    }
    protected void save() { controller.guardar(selected, id.getText(), nombre.getText(), documento.getText(), correo.getText(), telefono.getText()); }
}
