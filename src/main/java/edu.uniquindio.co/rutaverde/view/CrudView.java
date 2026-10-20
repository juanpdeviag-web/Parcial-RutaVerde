package edu.uniquindio.co.rutaverde.view;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import java.util.function.*;

/** Comportamiento común de los catálogos, sin dependencias del modelo. */
public abstract class CrudView<T> extends BorderPane {
    protected final TableView<T> table = new TableView<>();
    protected final VBox fields = new VBox(12);
    protected T selected;
    private final TextField search = new TextField();
    private final Label count = Ui.label("", "muted");
    private final Label status = Ui.label("", "success");
    private final Label formTitle = Ui.label("Nuevo registro", "section-title");
    private final List<Function<T, String>> searchable = new ArrayList<>();
    private Supplier<List<T>> source;
    private Consumer<T> delete;
    private SortedList<T> sorted;

    protected void init(String title, String subtitle, Supplier<List<T>> source, Consumer<T> delete) {
        this.source = source; this.delete = delete;
        getStyleClass().add("page");
        setTop(Ui.heading(title, subtitle));
        search.setPromptText("Buscar en el listado…");
        search.textProperty().addListener((o, a, b) -> filter());
        Button fresh = Ui.button("+ Nuevo", "primary", () -> { reset(); status.setText(""); });
        HBox toolbar = new HBox(12, search, count, fresh);
        HBox.setHgrow(search, Priority.ALWAYS); toolbar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        table.setPlaceholder(Ui.label("Aún no hay registros. Crea el primero desde el formulario.", "muted"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.getSelectionModel().selectedItemProperty().addListener((o, old, item) -> {
            selected = item;
            formTitle.setText(item == null ? "Nuevo registro" : "Editar registro");
            fill(item);
        });
        VBox list = Ui.card(toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        list.setMinWidth(350);
        Button save = Ui.button("Guardar", "primary", () -> {
            save(); reset(); refresh(); status.setText("Registro guardado correctamente.");
        });
        Button clear = Ui.button("Limpiar", "secondary", () -> { reset(); status.setText(""); });
        Button remove = Ui.button("Eliminar", "danger", () -> {
            T item = selected;
            if (item == null) throw new IllegalArgumentException("Selecciona un registro del listado.");
            if (Ui.confirm("Se eliminará el registro seleccionado.")) {
                this.delete.accept(item); reset(); refresh(); status.setText("Registro eliminado.");
            }
        });
        remove.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        VBox form = Ui.card(formTitle, fields, new HBox(8, save, clear), remove, status);
        ScrollPane formScroll = Ui.scroll(form); formScroll.setMinWidth(290);
        SplitPane split = new SplitPane(list, formScroll); split.setDividerPositions(0.65);
        BorderPane.setMargin(split, new javafx.geometry.Insets(24, 0, 0, 0));
        setCenter(split);
        refresh(); fill(null);
    }

    protected void field(String title, Node control) { fields.getChildren().add(Ui.field(title, control)); }
    protected void column(String title, Function<T, String> getter) {
        table.getColumns().add(Ui.column(title, getter)); searchable.add(getter);
    }
    public final void refresh() {
        FilteredList<T> filtered = new FilteredList<>(FXCollections.observableArrayList(source.get()));
        sorted = new SortedList<>(filtered); sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted); filter();
    }
    @SuppressWarnings("unchecked")
    private void filter() {
        if (sorted == null) return;
        String query = search.getText().trim().toLowerCase(Locale.ROOT);
        FilteredList<T> filtered = (FilteredList<T>) sorted.getSource();
        filtered.setPredicate(item -> query.isEmpty() || searchable.stream().anyMatch(getter ->
                Objects.toString(getter.apply(item), "").toLowerCase(Locale.ROOT).contains(query)));
        count.setText(filtered.size() + (filtered.size() == 1 ? " registro" : " registros"));
    }
    private void reset() {
        table.getSelectionModel().clearSelection(); selected = null; formTitle.setText("Nuevo registro"); fill(null);
    }
    protected abstract void fill(T item);
    protected abstract void save();
}
