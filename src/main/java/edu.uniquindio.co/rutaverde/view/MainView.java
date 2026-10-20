package edu.uniquindio.co.rutaverde.view;

import edu.uniquindio.co.rutaverde.controller.MainController;
import edu.uniquindio.co.rutaverde.controller.GuideController;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.util.function.Supplier;

public class MainView extends Application {
    private final MainController controller = new MainController();
    private final BorderPane root = new BorderPane();
    private final Map<Button, Supplier<javafx.scene.Node>> routes = new LinkedHashMap<>();

    @Override
    public void start(Stage stage) {
        VBox sidebar = new VBox(8);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(218); sidebar.setMinWidth(218);
        Label brand = Ui.label("RutaVerde", "brand");
        Label tag = Ui.label("EXPERIENCIAS QUE CONECTAN", "brand-tag");
        VBox logo = new VBox(6, brand, tag); logo.setPadding(new Insets(10, 0, 30, 0));
        sidebar.getChildren().add(logo);
        route(sidebar, "Inicio", this::home);
        route(sidebar, "Clientes", () -> new ClienteView(controller));
        route(sidebar, "Guías", () -> new GuiaView(controller));
        route(sidebar, "Actividades", () -> new ActividadView(controller));
        route(sidebar, "Productos", () -> new ProductoView(controller));
        route(sidebar, "Reservas", () -> new ReservaView(controller));
        route(sidebar, "Reportes", () -> new ReporteView(controller));
        route(sidebar, "Guía", () -> new GuideView(new GuideController(), this::navigateTo));
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().addAll(spacer, Ui.label("Turismo de naturaleza\nQuindío, Colombia", "sidebar-footer"));
        root.setLeft(sidebar);
        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/rutaverde.css")).toExternalForm());
        stage.setTitle("RutaVerde · Gestión de experiencias");
        stage.setMinWidth(1080); stage.setMinHeight(720);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            if (!Ui.confirm("Los datos de esta sesión se perderán al cerrar. ¿Cerrar RutaVerde?")) event.consume();
        });
        navigate(routes.keySet().iterator().next());
        stage.show();
    }

    private void route(VBox sidebar, String title, Supplier<javafx.scene.Node> page) {
        Button button = new Button(title); button.getStyleClass().add("nav-button");
        button.setId("nav-" + switch(title) {
            case "Guías" -> "guias"; case "Guía" -> "guia"; default -> title.toLowerCase(Locale.ROOT);
        });
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> Ui.run(() -> navigate(button)));
        routes.put(button, page); sidebar.getChildren().add(button);
    }
    private void navigate(Button target) {
        javafx.scene.Node page = routes.get(target).get();
        routes.keySet().forEach(b -> b.getStyleClass().remove("nav-active"));
        target.getStyleClass().add("nav-active");
        root.setCenter(page);
    }
    private void navigateTo(String title) {
        routes.keySet().stream().filter(button -> title.equals(button.getText())).findFirst().ifPresent(this::navigate);
    }
    private javafx.scene.Node home() {
        VBox page = new VBox(24); page.getStyleClass().add("page");
        page.getChildren().add(Ui.heading("Cada viaje empieza aquí", "Administra personas, experiencias y reservas desde un solo lugar."));
        VBox hero = new VBox(12, Ui.label("Explora. Conecta. Conserva.", "hero-title"),
                Ui.label("Organiza experiencias de naturaleza con todo lo necesario para acompañar a tus viajeros.", "hero-text"));
        hero.getStyleClass().add("hero");
        HBox metrics = new HBox(16, metric("Clientes", controller.clientes.listar().size()),
                metric("Guías", controller.guias.listar().size()), metric("Actividades", controller.actividades.listar().size()),
                metric("Reservas", controller.reservas.listar().size()));
        VBox flow = Ui.card(Ui.label("Prepara tu próxima experiencia", "section-title"),
                Ui.label("1. Registra clientes y guías.\n2. Crea actividades con precio, fecha y cupos.\n3. Agrupa actividades en un producto.\n4. Confirma la reserva y consulta su voucher.\n5. Revisa los resultados en Reportes.", "body-text"));
        Button example = Ui.button("Cargar ejemplo para la demostración", "secondary", () -> {
            boolean alreadyLoaded = controller.demoCargado();
            controller.cargarEjemplo(); root.setCenter(home());
            Ui.info(alreadyLoaded ? "Los datos demo ya estaban cargados. No se duplicaron registros ni pagos."
                    : "Demo cargado: 10 clientes, 10 guías, 10 actividades, 10 productos y 10 reservas pagadas mediante PagoAndino.");
        });
        example.setId("home-demo");
        boolean occupied = !controller.clientes.listar().isEmpty() || !controller.guias.listar().isEmpty()
                || !controller.actividades.listar().isEmpty() || !controller.productos.listar().isEmpty()
                || !controller.reservas.listar().isEmpty();
        example.setDisable(occupied && !controller.demoCargado());
        Button guide = Ui.button("Guía de uso", "primary", () -> navigateTo("Guía"));
        guide.setId("home-guide");
        page.getChildren().addAll(hero, metrics, flow, new FlowPane(12, 10, guide, example),
                Ui.label("Datos en memoria: se conservan al navegar y se pierden al cerrar. Los pagos y correos son simulados.", "muted"));
        return Ui.scroll(page);
    }
    private VBox metric(String title, int value) {
        VBox card = Ui.card(Ui.label(String.valueOf(value), "metric-value"), Ui.label(title, "muted"));
        card.setPrefWidth(220); HBox.setHgrow(card, Priority.ALWAYS); return card;
    }
}
