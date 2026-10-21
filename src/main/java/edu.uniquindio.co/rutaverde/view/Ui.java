package edu.uniquindio.co.rutaverde.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

public final class Ui {
    private Ui() {}
    public static Label label(String text, String style) {
        Label label = new Label(text); label.getStyleClass().add(style); label.setWrapText(true); return label;
    }
    public static VBox heading(String title, String description) {
        return new VBox(6, label(title, "page-title"), label(description, "muted"));
    }
    public static Button button(String text, String style, Runnable action) {
        Button button = new Button(text);
        if (!style.isBlank()) button.getStyleClass().add(style);
        button.setOnAction(e -> run(action));
        return button;
    }
    public static void run(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | ArithmeticException ex) { error(ex.getMessage()); }
    }
    public static void error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message == null ? "Revisa los datos e intenta de nuevo." : message, ButtonType.OK);
        alert.setTitle("Revisa la información"); alert.setHeaderText("No se pudo completar la acción"); alert.showAndWait();
    }
    public static boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Confirmar acción"); alert.setHeaderText("¿Deseas continuar?");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    public static void info(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("RutaVerde"); alert.setHeaderText(null); alert.showAndWait();
    }
    public static VBox field(String label, Node input) {
        if (input instanceof Region region) region.setMaxWidth(Double.MAX_VALUE);
        return new VBox(5, label(label, "field-label"), input);
    }
    public static String money(long value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        format.setMaximumFractionDigits(0);
        return format.format(value) + " COP";
    }
    public static <T> TableColumn<T, String> column(String title, Function<T, String> value) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(value.apply(cell.getValue())));
        column.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String text, boolean empty) {
                super.updateItem(text, empty);
                setText(empty ? null : text);
                setTooltip(empty || text == null || text.isBlank() ? null : new Tooltip(text));
            }
        });
        column.setPrefWidth(155);
        return column;
    }
    public static <T> void names(ComboBox<T> combo, Function<T, String> display) {
        combo.setConverter(new StringConverter<>() {
            public String toString(T value) { return value == null ? "" : display.apply(value); }
            public T fromString(String value) { return null; }
        });
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setPromptText("Seleccionar…");
    }
    public static ScrollPane scroll(Node content) {
        ScrollPane scroll = new ScrollPane(content); scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); return scroll;
    }
    public static VBox card(Node... children) {
        VBox card = new VBox(14, children); card.getStyleClass().add("card"); return card;
    }
}
