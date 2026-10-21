package edu.uniquindio.co.rutaverde.view;

import edu.uniquindio.co.rutaverde.controller.*;
import edu.uniquindio.co.rutaverde.model.entities.Reserva;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

/**
 * Verificación manual automatizada para un escritorio con JavaFX disponible.
 * No forma parte de Surefire: no exige una pantalla para ejecutar las pruebas unitarias.
 */
public class JavaFxSmokeCheck {
    public static void main(String[] args) throws Exception {
        Platform.setImplicitExit(false);
        CountDownLatch ready = new CountDownLatch(1);
        Platform.startup(ready::countDown);
        if (!ready.await(20, TimeUnit.SECONDS)) throw new IllegalStateException("JavaFX no inició.");
        FutureTask<Void> check = new FutureTask<>(() -> {
            Stage stage = new Stage();
            try {
                new MainView().start(stage);
                require(stage.isShowing(), "La ventana principal no se mostró.");
                BorderPane root = (BorderPane) stage.getScene().getRoot();
                for (String route : List.of("inicio", "clientes", "guias", "actividades", "productos", "reservas", "reportes", "guia")) {
                    Button button = (Button) root.lookup("#nav-" + route);
                    require(button != null, "No existe la navegación: " + route);
                    button.fire();
                    require(root.getCenter() != null, "Pantalla vacía: " + route);
                    layout(stage.getScene());
                }
                ((Button) root.lookup("#nav-inicio")).fire();
                layout(stage.getScene());
                javafx.scene.Node sidebar = root.getLeft();
                ((Button) root.lookup("#home-guide")).fire();
                require(root.getCenter() instanceof GuideView, "Inicio no abre la Guía de uso.");
                require(sidebar == root.getLeft(), "La guía cambió el menú lateral.");
                GuideView guide = (GuideView) root.getCenter();
                layout(stage.getScene());
                snapshot(stage.getScene(), "guia");
                for (int i = 1; i <= 10; i++) {
                    javafx.scene.Node section = guide.lookup("#guide-section-" + i);
                    require(section != null, "Falta la sección de guía " + i);
                    double recorrido = guide.getContent().getBoundsInLocal().getHeight() - guide.getViewportBounds().getHeight();
                    guide.setVvalue(Math.min(1, section.getBoundsInParent().getMinY() / recorrido));
                    layout(stage.getScene());
                }
                guide.setVvalue(1); layout(stage.getScene());
                snapshot(stage.getScene(), "guia-final");
                ((Button) guide.lookup("#guide-open-6")).fire();
                require(root.getCenter() instanceof ReservaView, "La guía no permite ir a Reservas.");
                ((Button) root.lookup("#nav-guia")).fire();
                layout(stage.getScene());
                ((Button) root.lookup("#guide-home")).fire();
                layout(stage.getScene());
                require(root.lookup("#home-demo") != null, "La guía no regresó al Inicio.");
                ((Button) root.lookup("#nav-reservas")).fire();
                layout(stage.getScene());
                registrarTarjeta(root);
                ((Button) root.lookup("#nav-inicio")).fire();
                layout(stage.getScene());
                for (int i = 0; i < 2; i++) {
                    acceptNextDialog();
                    ((Button) root.lookup("#home-demo")).fire();
                    layout(stage.getScene());
                }
                snapshot(stage.getScene(), "inicio-demo");
                for (String route : List.of("clientes", "guias", "actividades", "productos", "reservas")) {
                    ((Button) root.lookup("#nav-" + route)).fire();
                    layout(stage.getScene());
                    TableView<?> demoTable = (TableView<?>) root.getCenter().lookup(".table-view");
                    require(demoTable.getItems().size() == 10, "Carga demo incorrecta o duplicada: " + route);
                    if ("reservas".equals(route))
                        require(((ComboBox<?>) root.lookup("#reserva-tarjetas")).getItems().size() == 2,
                                "Cargar el demo alteró las tarjetas registradas manualmente.");
                }
                ((Button) root.lookup("#nav-inicio")).fire();
                snapshot(stage.getScene(), "inicio");

                MainController app = new MainController();
                app.cargarEjemplo();
                // Ejercita la selección y edición del catálogo con datos reales.
                ClienteView clientes = new ClienteView(app);
                root.setCenter(clientes);
                layout(stage.getScene());
                clientes.table.getSelectionModel().selectFirst();
                require(clientes.fields.lookupAll(".text-field").stream()
                        .anyMatch(n -> n instanceof TextField f && "María López".equals(f.getText())),
                        "La selección no llenó el formulario de cliente.");
                TextField nombre = clientes.fields.lookupAll(".text-field").stream()
                        .filter(n -> n instanceof TextField f && "María López".equals(f.getText()))
                        .map(n -> (TextField) n).findFirst().orElseThrow();
                nombre.setText("María López (editada)");
                Button guardar = clientes.lookupAll(".button").stream()
                        .filter(n -> n instanceof Button b && "Guardar".equals(b.getText()))
                        .map(n -> (Button) n).findFirst().orElseThrow();
                guardar.fire();
                require("María López (editada)".equals(app.clientes.buscar("C001").getNombre()),
                        "El botón Guardar no actualizó el repositorio.");
                GuiaView guias = new GuiaView(app);
                root.setCenter(guias); layout(stage.getScene()); guias.table.getSelectionModel().selectFirst();
                ActividadView actividades = new ActividadView(app);
                root.setCenter(actividades); layout(stage.getScene()); actividades.table.getSelectionModel().selectFirst();
                ProductoView productos = new ProductoView(app);
                root.setCenter(productos); layout(stage.getScene()); productos.table.getSelectionModel().selectFirst();
                snapshot(stage.getScene(), "productos");
                root.setCenter(new ReservaView(app)); layout(stage.getScene());
                require(hasLabel(root.getCenter(), "Pasarela de pago: PagoAndino"),
                        "El formulario no identifica PagoAndino.");
                registrarTarjeta(root);
                require(app.tarjetas.buscar("4111111111111111") != null, "La tarjeta no llegó al repositorio.");
                // Abre nuevamente Reservas para verificar el uso posterior de la tarjeta guardada.
                root.setCenter(new ReservaView(app)); layout(stage.getScene());
                ComboBox<?> tarjetas = (ComboBox<?>) root.lookup("#reserva-tarjetas");
                require(tarjetas.getItems().size() == 2, "La tarjeta no se conservó al volver a Reservas.");
                tarjetas.getSelectionModel().selectLast();
                ((ComboBox<?>) root.lookup("#reserva-cliente")).getSelectionModel().selectFirst();
                ((ComboBox<?>) root.lookup("#reserva-producto")).getSelectionModel().selectFirst();
                ((DatePicker) root.lookup("#reserva-fecha")).setValue(app.actividades.buscar("A001").getFecha());
                require("4111111111111111".equals(((PasswordField) root.lookup("#reserva-tarjeta")).getText()),
                        "El pago no está usando la tarjeta registrada manualmente.");
                // Acepta únicamente la confirmación mostrada por el botón real de pago.
                Platform.runLater(() -> {
                    for (Window window : List.copyOf(Window.getWindows())) {
                        if (window.getScene() != null && window.getScene().getRoot() instanceof DialogPane pane
                                && pane.getButtonTypes().contains(ButtonType.CANCEL)
                                && pane.getButtonTypes().contains(ButtonType.OK)) {
                            ((Button) pane.lookupButton(ButtonType.OK)).fire();
                        }
                    }
                });
                ((Button) root.lookup("#reserva-confirmar")).fire();
                Reserva reserva = app.reservas.buscar(11);
                require(reserva != null, "No se creó la reserva de demostración.");
                Label resultado = (Label) root.lookup("#reserva-resultado");
                require(resultado.getText().contains("Pago procesado correctamente mediante PagoAndino"),
                        "El resultado no identifica PagoAndino.");
                require(resultado.getText().contains("PA-1011")
                                && resultado.getText().contains(Ui.money(reserva.getValorPagado()))
                                && resultado.getText().contains("CONFIRMADA"),
                        "El resultado no muestra autorización, valor y estado.");
                require("PA-1011".equals(app.pasarela.obtenerAutorizacion()), "El pago no pasó por la API.");
                layout(stage.getScene());
                TableView<?> table = (TableView<?>) root.getCenter().lookup(".table-view");
                require(table.getItems().size() == 11, "La nueva reserva no aparece junto a las diez demo.");
                table.getSelectionModel().selectLast();
                require(root.getCenter().lookupAll(".text-area").stream()
                        .anyMatch(n -> n instanceof TextArea a && a.getText().contains("VOUCHER DETALLADO")
                                && a.getText().contains("Pasarela: PagoAndino")),
                        "No se mostró el voucher.");
                snapshot(stage.getScene(), "reservas");
                tarjetas.getSelectionModel().selectFirst();
                require(TarjetaController.NUMERO_DEMO.equals(((PasswordField) root.lookup("#reserva-tarjeta")).getText()),
                        "La tarjeta demo dejó de estar disponible.");
                acceptNextDialog();
                ((Button) root.lookup("#reserva-confirmar")).fire();
                require(app.reservas.listar().size() == 12 && "PA-1012".equals(app.pasarela.obtenerAutorizacion()),
                        "La tarjeta demo no pagó por el mismo flujo.");
                root.setCenter(new ReporteView(app)); layout(stage.getScene());
                require(hasLabel(root.getCenter(), "Pasarela: PagoAndino"),
                        "El reporte no identifica PagoAndino.");
                require(hasLabel(root.getCenter(), Ui.money(1780000L)), "El reporte no incluye ambos pagos.");
                String ayuda = new GuideController().secciones().stream().map(GuideController.Section::contenido)
                        .collect(java.util.stream.Collectors.joining(" "));
                require(ayuda.contains("Agregar tarjeta") && ayuda.contains("Guardar tarjeta") && ayuda.contains("Tarjeta registrada"),
                        "La guía no explica el registro y selección de tarjetas.");
                snapshot(stage.getScene(), "reportes");
                System.out.println("JAVAFX_SMOKE_OK: tarjeta manual registrada y seleccionada; demo preservado; pagos manual y demo mediante PagoAndino; vouchers, reportes y guía.");
            } finally { stage.hide(); }
            return null;
        });
        Platform.runLater(check);
        try { check.get(45, TimeUnit.SECONDS); }
        finally { Platform.exit(); }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
    private static void registrarTarjeta(BorderPane root) {
        ((Button) root.lookup("#tarjeta-agregar")).fire();
        layout(root.getScene());
        ((PasswordField) root.lookup("#tarjeta-nuevo-numero")).setText("4111 1111 1111 1111");
        ((Button) root.lookup("#tarjeta-guardar")).fire();
        layout(root.getScene());
        ComboBox<?> tarjetas = (ComboBox<?>) root.lookup("#reserva-tarjetas");
        require(tarjetas.getItems().size() == 2 && tarjetas.getValue() != null,
                "Guardar tarjeta no actualizó el selector.");
        require(hasLabel(root.getCenter(), "Tarjeta registrada. Ya puedes seleccionarla para pagar."),
                "No se mostró la confirmación del registro de tarjeta.");
    }
    private static void acceptNextDialog() {
        Platform.runLater(() -> {
            for (Window window : List.copyOf(Window.getWindows())) {
                if (window.getScene() != null && window.getScene().getRoot() instanceof DialogPane pane
                        && pane.getButtonTypes().contains(ButtonType.OK)) {
                    ((Button) pane.lookupButton(ButtonType.OK)).fire();
                }
            }
        });
    }
    private static boolean hasLabel(javafx.scene.Node node, String text) {
        return node.lookupAll(".label").stream().anyMatch(n -> n instanceof Label l && l.getText().contains(text));
    }
    private static void layout(Scene scene) {
        scene.getRoot().applyCss();
        scene.getRoot().layout();
    }
    private static void snapshot(Scene scene, String name) throws Exception {
        layout(scene);
        javafx.scene.image.WritableImage image = scene.getRoot().snapshot(null, null);
        BufferedImage png = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < png.getHeight(); y++)
            for (int x = 0; x < png.getWidth(); x++) png.setRGB(x, y, image.getPixelReader().getArgb(x, y));
        Path directory = Path.of("target", "ui-preview");
        Files.createDirectories(directory);
        ImageIO.write(png, "png", directory.resolve(name + ".png").toFile());
    }
}
