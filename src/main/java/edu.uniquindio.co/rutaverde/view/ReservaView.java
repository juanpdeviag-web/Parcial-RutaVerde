package edu.uniquindio.co.rutaverde.view;

import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.time.LocalDate;
import java.util.Locale;

public class ReservaView extends BorderPane {
    private final ReservaController controller;
    private final TarjetaController tarjetaController;
    private final TableView<Reserva> table = new TableView<>();
    private final ComboBox<Cliente> cliente = new ComboBox<>();
    private final ComboBox<Producto> producto = new ComboBox<>();
    private final DatePicker fecha = new DatePicker(LocalDate.now().plusDays(1));
    private final PasswordField tarjeta = new PasswordField();
    private final ComboBox<Tarjeta> tarjetas = new ComboBox<>();
    private final ComboBox<String> tipo = new ComboBox<>();
    private final TextArea contenido = new TextArea();
    private final Label detalle = Ui.label("Selecciona un producto para consultar su precio y cupos.", "muted");
    private final Label status = Ui.label("", "success");
    private final TextField buscar = new TextField();
    private final ComboBox<String> estado = new ComboBox<>();
    private FilteredList<Reserva> filtered;

    public ReservaView(MainController app) {
        controller = new ReservaController(app);
        tarjetaController = new TarjetaController(app);
        getStyleClass().add("page");
        setTop(Ui.heading("Reservas", "Una reserva corresponde a una persona. Consulta el voucher desde el listado."));
        Ui.names(cliente, c -> c.getNombre() + " · " + c.getIdentificacion());
        Ui.names(producto, p -> p.getNombre() + " · " + p.getId());
        cliente.getItems().setAll(controller.clientes());
        producto.getItems().setAll(controller.productos());
        producto.valueProperty().addListener((o, a, p) -> updateDetail());
        fecha.setEditable(false);
        tarjeta.setPromptText("Número de tarjeta");
        cliente.setId("reserva-cliente"); producto.setId("reserva-producto");
        fecha.setId("reserva-fecha"); tarjeta.setId("reserva-tarjeta");
        Ui.names(tarjetas, tarjetaController::etiqueta);
        tarjetas.setId("reserva-tarjetas");
        tarjetas.setPromptText("Selecciona una tarjeta guardada");
        tarjetas.getItems().setAll(tarjetaController.listar());
        tarjetas.valueProperty().addListener((o, anterior, elegida) -> {
            if (elegida != null) tarjeta.setText(elegida.getNumero());
        });
        tarjeta.textProperty().addListener((o, anterior, numero) -> {
            if (tarjetas.getValue() != null && !numero.equals(tarjetas.getValue().getNumero()))
                tarjetas.getSelectionModel().clearSelection();
        });
        PasswordField nuevoNumero = new PasswordField();
        nuevoNumero.setId("tarjeta-nuevo-numero");
        nuevoNumero.setPromptText("Número de tarjeta");
        VBox registroTarjeta = new VBox(10);
        registroTarjeta.setVisible(false); registroTarjeta.setManaged(false);
        Button guardarTarjeta = Ui.button("Guardar tarjeta", "primary", () -> {
            Tarjeta nueva = tarjetaController.agregar(nuevoNumero.getText());
            tarjetas.getItems().setAll(tarjetaController.listar());
            tarjetas.setValue(nueva);
            nuevoNumero.clear();
            registroTarjeta.setVisible(false); registroTarjeta.setManaged(false);
            status.setText("Tarjeta registrada. Ya puedes seleccionarla para pagar.");
        });
        guardarTarjeta.setId("tarjeta-guardar");
        Button cancelarRegistro = Ui.button("Cancelar", "secondary", () -> {
            nuevoNumero.clear();
            registroTarjeta.setVisible(false); registroTarjeta.setManaged(false);
        });
        registroTarjeta.getChildren().addAll(Ui.field("Número de tarjeta *", nuevoNumero),
                new FlowPane(8, 8, guardarTarjeta, cancelarRegistro));
        Button agregarTarjeta = Ui.button("Agregar tarjeta", "secondary", () -> {
            registroTarjeta.setVisible(true); registroTarjeta.setManaged(true);
            nuevoNumero.requestFocus();
        });
        agregarTarjeta.setId("tarjeta-agregar");
        Button confirmar = Ui.button("Confirmar y pagar", "primary", () -> {
            status.setText("");
            Producto elegido = producto.getValue();
            if (cliente.getValue() == null || elegido == null || fecha.getValue() == null)
                throw new IllegalArgumentException("Selecciona cliente, producto y fecha.");
            if (!Ui.confirm("Confirmar una reserva para " + cliente.getValue().getNombre()
                    + " por " + Ui.money(elegido.calcularPrecio()) + " mediante PagoAndino (API simulada del proyecto).")) return;
            Reserva reserva;
            try { reserva = controller.confirmar(cliente.getValue(), elegido, fecha.getValue(), tarjeta.getText()); }
            finally { tarjeta.clear(); }
            buscar.clear();
            estado.setValue("Todos los estados");
            refresh();
            table.getSelectionModel().select(reserva);
            updateDetail();
            status.setText("Pago procesado correctamente mediante PagoAndino\n"
                    + "Valor pagado: " + Ui.money(reserva.getValorPagado()) + "\n"
                    + "Estado de la reserva: " + reserva.getEstado() + "\n"
                    + "Autorización PagoAndino: " + controller.ultimaAutorizacionPago() + "\n"
                    + "Reserva #" + reserva.getNumero() + ". Voucher emitido y correo simulado.");
        });
        confirmar.setId("reserva-confirmar");
        status.setId("reserva-resultado");
        VBox form = Ui.card(Ui.label("Nueva reserva", "section-title"),
                Ui.field("Cliente *", cliente), Ui.field("Producto *", producto), detalle,
                Ui.field("Fecha de viaje *", fecha),
                Ui.label("Pasarela de pago: PagoAndino", "field-label"),
                Ui.field("Tarjeta registrada", tarjetas), agregarTarjeta, registroTarjeta,
                Ui.field("Tarjeta *", tarjeta),
                Ui.label("Selecciona una tarjeta guardada o escribe su número para este pago. Las tarjetas se conservan hasta cerrar.", "muted"),
                Ui.label("PagoAndino utiliza la API simulada del proyecto. Correo simulado. Tarjeta de prueba: 4539 1488 0343 6467.", "muted"),
                confirmar, status);
        form.setMinWidth(290);
        buscar.setPromptText("Buscar por número, cliente o producto…");
        estado.getItems().addAll("Todos los estados", "CONFIRMADA", "CANCELADA", "ERROR");
        estado.setValue("Todos los estados");
        buscar.textProperty().addListener((o, a, b) -> filter());
        estado.valueProperty().addListener((o, a, b) -> filter());
        HBox toolbar = new HBox(10, buscar, estado); HBox.setHgrow(buscar, Priority.ALWAYS);
        table.getColumns().add(Ui.column("N.º", r -> String.valueOf(r.getNumero())));
        table.getColumns().add(Ui.column("Cliente", r -> r.getCliente().getNombre()));
        table.getColumns().add(Ui.column("Producto", r -> r.getProducto().getNombre()));
        table.getColumns().add(Ui.column("Viaje", r -> String.valueOf(r.getFechaViaje())));
        table.getColumns().add(Ui.column("Estado", Reserva::getEstado));
        table.getColumns().add(Ui.column("Valor", r -> Ui.money(r.getValorPagado())));
        table.getColumns().get(0).setPrefWidth(50);
        table.getColumns().get(0).setMaxWidth(65);
        table.getColumns().get(3).setPrefWidth(105);
        table.getColumns().get(4).setPrefWidth(110);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPlaceholder(Ui.label("No hay reservas para mostrar.", "muted"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, r) -> updateVoucher());
        tipo.getItems().addAll("DETALLADO", "CORTO", "MOVIL"); tipo.setValue("DETALLADO");
        tipo.valueProperty().addListener((o, a, b) -> updateVoucher());
        Button cancelar = Ui.button("Cancelar reserva", "danger", () -> {
            Reserva r = table.getSelectionModel().getSelectedItem();
            if (Ui.confirm("La reserva cambiará a CANCELADA. La lógica actual no realiza reembolsos ni devuelve cupos automáticamente.")) {
                controller.cancelar(r); refresh(); table.getSelectionModel().select(r);
                status.setText("Reserva #" + r.getNumero() + " cancelada.");
            }
        });
        cancelar.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        contenido.setEditable(false); contenido.setWrapText(true); contenido.setPrefRowCount(7);
        contenido.setPromptText("Selecciona una reserva para consultar el comprobante.");
        Button copiar = Ui.button("Copiar voucher", "secondary", () -> {
            if (contenido.getText().isBlank()) throw new IllegalArgumentException("Selecciona una reserva primero.");
            ClipboardContent content = new ClipboardContent(); content.putString(contenido.getText());
            Clipboard.getSystemClipboard().setContent(content); status.setText("Voucher copiado.");
        });
        VBox list = Ui.card(toolbar, table, new HBox(10, cancelar, tipo, copiar), contenido);
        list.setMinWidth(420); VBox.setVgrow(table, Priority.ALWAYS);
        SplitPane split = new SplitPane(list, Ui.scroll(form)); split.setDividerPositions(0.67);
        BorderPane.setMargin(split, new Insets(24, 0, 0, 0)); setCenter(split);
        refresh();
    }

    private void updateDetail() {
        Producto p = producto.getValue();
        if (p == null) { detalle.setText("Selecciona un producto."); return; }
        int cupos = p.getActividades().stream().mapToInt(Actividad::getCupoDisponible).min().orElse(0);
        detalle.setText(Ui.money(p.calcularPrecio()) + " por persona · " + cupos + " cupos\n"
                + p.getActividades().stream().map(a -> a.getNombre() + " (" + a.getFecha() + ")")
                        .collect(java.util.stream.Collectors.joining(", ")));
    }
    private void refresh() {
        filtered = new FilteredList<>(FXCollections.observableArrayList(controller.listar()));
        SortedList<Reserva> sorted = new SortedList<>(filtered); sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted); filter();
    }
    private void filter() {
        if (filtered == null) return;
        String query = buscar.getText().trim().toLowerCase(Locale.ROOT);
        filtered.setPredicate(r -> ("Todos los estados".equals(estado.getValue()) || r.getEstado().equals(estado.getValue()))
                && (r.getNumero() + " " + r.getCliente().getNombre() + " " + r.getProducto().getNombre())
                        .toLowerCase(Locale.ROOT).contains(query));
    }
    private void updateVoucher() {
        Reserva r = table.getSelectionModel().getSelectedItem();
        contenido.setText(r == null ? "" : controller.voucher(r, tipo.getValue()));
    }
}
