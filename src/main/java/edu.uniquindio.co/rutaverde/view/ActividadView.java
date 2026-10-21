package edu.uniquindio.co.rutaverde.view;
import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.*;
import javafx.scene.control.*;
import java.time.LocalDate;

public class ActividadView extends CrudView<Actividad> {
    private final ActividadController controller;
    private final TextField id = new TextField(), nombre = new TextField(), cupos = new TextField(),
            precio = new TextField(), servicios = new TextField();
    private final TextArea descripcion = new TextArea();
    private final DatePicker fecha = new DatePicker();
    private final ComboBox<Guia> guia = new ComboBox<>();
    public ActividadView(MainController app) {
        controller = new ActividadController(app);
        column("ID", Actividad::getId); column("Actividad", Actividad::getNombre);
        column("Fecha", a -> String.valueOf(a.getFecha()));
        column("Guía", a -> a.getGuia() == null ? "Sin asignar" : a.getGuia().getNombre());
        column("Cupos", a -> String.valueOf(a.getCupoDisponible()));
        column("Precio", a -> Ui.money(a.getPrecio()));
        descripcion.setPrefRowCount(3); descripcion.setWrapText(true);
        fecha.setEditable(false);
        Ui.names(guia, g -> g.getNombre() + " · " + g.getId());
        guia.getItems().setAll(controller.guias());
        field("ID *", id); field("Nombre *", nombre); field("Descripción", descripcion);
        field("Fecha *", fecha); field("Guía *", guia); field("Cupos disponibles *", cupos);
        field("Precio por persona (COP, entero) *", precio); field("Servicios, separados por coma", servicios);
        init("Actividades", "Experiencias, guías y disponibilidad. Registra primero los guías.", controller::listar, controller::eliminar);
    }
    protected void fill(Actividad a) {
        id.setText(a == null ? "" : a.getId()); id.setDisable(a != null);
        nombre.setText(a == null ? "" : a.getNombre()); descripcion.setText(a == null ? "" : a.getDescripcion());
        fecha.setValue(a == null ? LocalDate.now().plusDays(1) : a.getFecha());
        guia.setValue(a == null ? null : a.getGuia());
        cupos.setText(a == null ? "" : String.valueOf(a.getCupoDisponible()));
        precio.setText(a == null ? "" : String.valueOf(a.getPrecio()));
        servicios.setText(a == null ? "" : String.join(", ", a.getServiciosIncluidos()));
    }
    protected void save() {
        controller.guardar(selected, id.getText(), nombre.getText(), descripcion.getText(),
                cupos.getText(), fecha.getValue(), guia.getValue(), servicios.getText(), precio.getText());
    }
}
