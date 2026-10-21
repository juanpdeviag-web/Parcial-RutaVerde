package edu.uniquindio.co.rutaverde.view;
import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.Guia;
import javafx.scene.control.TextField;

public class GuiaView extends CrudView<Guia> {
    private final GuiaController controller;
    private final TextField id = new TextField(), nombre = new TextField(), documento = new TextField(), especialidades = new TextField();
    public GuiaView(MainController app) {
        controller = new GuiaController(app);
        column("ID", Guia::getId); column("Nombre", Guia::getNombre);
        column("Identificación", Guia::getIdentificacion);
        column("Especialidades", g -> String.join(", ", g.getEspecialidades()));
        field("ID *", id); field("Nombre completo *", nombre); field("Identificación *", documento);
        field("Especialidades, separadas por coma", especialidades);
        especialidades.setPromptText("Senderismo, avistamiento…");
        init("Guías", "El equipo que acompaña cada experiencia.", controller::listar, controller::eliminar);
    }
    protected void fill(Guia g) {
        id.setText(g == null ? "" : g.getId()); id.setDisable(g != null);
        nombre.setText(g == null ? "" : g.getNombre()); documento.setText(g == null ? "" : g.getIdentificacion());
        especialidades.setText(g == null ? "" : String.join(", ", g.getEspecialidades()));
    }
    protected void save() { controller.guardar(selected, id.getText(), nombre.getText(), documento.getText(), especialidades.getText()); }
}
