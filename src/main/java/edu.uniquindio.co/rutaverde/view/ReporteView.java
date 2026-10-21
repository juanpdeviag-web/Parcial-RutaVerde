package edu.uniquindio.co.rutaverde.view;

import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.Producto;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class ReporteView extends VBox {
    private final ReporteController controller;
    private final DatePicker inicio = new DatePicker(LocalDate.now().withDayOfMonth(1));
    private final DatePicker fin = new DatePicker(LocalDate.now());
    private final Label ingresos = Ui.label("", "metric-value");
    private final Label confirmadas = Ui.label("", "metric-value");
    private final Label popular = Ui.label("", "metric-value");
    private final Label actualizado = Ui.label("", "muted");
    private final TableView<Producto> table = new TableView<>();

    public ReporteView(MainController app) {
        controller = new ReporteController(app);
        getStyleClass().add("page"); setSpacing(24);
        inicio.setEditable(false); fin.setEditable(false);
        HBox fechas = new HBox(12, Ui.field("Desde", inicio), Ui.field("Hasta", fin),
                Ui.button("Actualizar reporte", "primary", this::refresh));
        fechas.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
        HBox metrics = new HBox(16,
                metric("Ingresos del período", ingresos, "Reservas confirmadas por fecha de registro"),
                metric("Reservas confirmadas", confirmadas, "Total de la sesión"),
                metric("Producto más vendido", popular, "Total de la sesión"));
        TableColumn<Producto, String> producto = Ui.column("Producto", Producto::getNombre);
        TableColumn<Producto, String> viajeros = Ui.column("Viajeros confirmados", p -> String.valueOf(controller.viajeros(p)));
        table.getColumns().add(producto); table.getColumns().add(viajeros);
        table.getColumns().add(Ui.column("Precio actual por persona", p -> Ui.money(p.calcularPrecio())));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(Ui.label("Los productos aparecerán aquí cuando los registres.", "muted"));
        VBox detail = Ui.card(Ui.label("Viajeros por producto", "section-title"),
                Ui.label("Acumulado de la sesión. Cada reserva confirmada cuenta como un viajero.", "muted"), table);
        VBox.setVgrow(table, Priority.ALWAYS); VBox.setVgrow(detail, Priority.ALWAYS);
        getChildren().addAll(Ui.heading("Reportes", "Pasarela: PagoAndino · Pagos de reservas procesados mediante PagoAndino."),
                fechas, metrics, actualizado, detail);
        refresh();
    }
    private VBox metric(String title, Label value, String description) {
        VBox card = Ui.card(Ui.label(title, "field-label"), value, Ui.label(description, "muted"));
        HBox.setHgrow(card, Priority.ALWAYS); card.setPrefWidth(250); card.setMinWidth(0); return card;
    }
    private void refresh() {
        long total = controller.ingresos(inicio.getValue(), fin.getValue());
        ingresos.setText(Ui.money(total));
        confirmadas.setText(String.valueOf(controller.confirmadas()));
        Producto p = controller.masVendido(); popular.setText(p == null ? "Sin ventas" : p.getNombre());
        table.setItems(FXCollections.observableArrayList(controller.productos()));
        actualizado.setText("Ingresos del " + inicio.getValue() + " al " + fin.getValue()
                + " · Se excluyen reservas canceladas.");
    }
}
