package edu.uniquindio.co.rutaverde.view;
import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.*;
import javafx.scene.control.*;
import javafx.collections.ListChangeListener;
import java.util.ArrayList;

public class ProductoView extends CrudView<Producto> {
    private final ProductoController controller;
    private final TextField id = new TextField(), nombre = new TextField();
    private final TextArea descripcion = new TextArea();
    private final ListView<Actividad> actividades = new ListView<>();
    private final Label total = Ui.label("", "total");
    public ProductoView(MainController app) {
        controller = new ProductoController(app);
        column("ID", Producto::getId); column("Producto", Producto::getNombre);
        column("Actividades", p -> String.valueOf(p.getActividades().size()));
        column("Precio por persona", p -> Ui.money(p.calcularPrecio()));
        column("Disponibilidad", p -> p.tieneCupo(1) ? "Disponible" : "Sin cupo");
        descripcion.setPrefRowCount(3); descripcion.setWrapText(true);
        actividades.getItems().setAll(controller.actividades());
        actividades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actividades.setPrefHeight(200);
        actividades.setPlaceholder(Ui.label("Registra actividades primero.", "muted"));
        actividades.setCellFactory(list -> new ListCell<>() {
            protected void updateItem(Actividad a, boolean empty) {
                super.updateItem(a, empty);
                setText(empty || a == null ? null : a.getNombre() + " · " + Ui.money(a.getPrecio()));
            }
        });
        actividades.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Actividad>) change -> updateTotal());
        field("ID *", id); field("Nombre del paquete *", nombre); field("Descripción", descripcion);
        field("Actividades * (Ctrl o Mayús para seleccionar varias)", actividades);
        fields.getChildren().add(total);
        init("Productos", "Arma paquetes con las actividades del catálogo. El precio es la suma de sus actividades.",
                controller::listar, controller::eliminar);
    }
    private void updateTotal() {
        total.setText("Total: " + Ui.money(actividades.getSelectionModel().getSelectedItems().stream().mapToLong(Actividad::getPrecio).sum()));
    }
    protected void fill(Producto p) {
        id.setText(p == null ? "" : p.getId()); id.setDisable(p != null);
        nombre.setText(p == null ? "" : p.getNombre()); descripcion.setText(p == null ? "" : p.getDescripcion());
        actividades.getSelectionModel().clearSelection();
        if (p != null) p.getActividades().forEach(a -> actividades.getSelectionModel().select(a));
        updateTotal();
    }
    protected void save() {
        controller.guardar(selected, id.getText(), nombre.getText(), descripcion.getText(),
                new ArrayList<>(actividades.getSelectionModel().getSelectedItems()));
    }
}
