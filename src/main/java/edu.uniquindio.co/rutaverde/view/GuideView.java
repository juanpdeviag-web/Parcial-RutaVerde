package edu.uniquindio.co.rutaverde.view;

import edu.uniquindio.co.rutaverde.controller.GuideController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.function.Consumer;

/** Pantalla de ayuda dentro del contenedor principal, sin ventanas adicionales. */
public class GuideView extends ScrollPane {
    public GuideView(GuideController controller, Consumer<String> navigate) {
        setId("guide-scroll");
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        VBox page = new VBox(24);
        page.getStyleClass().add("page");
        Button back = Ui.button("Volver a Inicio", "secondary", () -> navigate.accept("Inicio"));
        back.setId("guide-home");
        page.getChildren().addAll(Ui.heading("Guía de uso de RutaVerde", "Aprende qué completar y qué botones usar, paso a paso."), back);
        FlowPane index = new FlowPane(8, 8);
        VBox contents = Ui.card(Ui.label("Encuentra lo que necesitas", "section-title"), index,
                Ui.label("Desplázate para leer la guía completa o elige un tema. El menú lateral sigue disponible.", "muted"));
        for (GuideController.Section section : controller.secciones()) {
            Label body = Ui.label(section.contenido().strip(), "body-text");
            body.setMaxWidth(Double.MAX_VALUE);
            body.setMinHeight(Region.USE_PREF_SIZE);
            if (section.numero() == 1) {
                body.setAlignment(Pos.CENTER);
                body.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            }
            Button open = Ui.button("Ir a " + section.destino(), "secondary", () -> navigate.accept(section.destino()));
            open.setId("guide-open-" + section.numero());
            String title = section.numero() == 1 ? section.titulo() : (section.numero() - 1) + ". " + section.titulo();
            VBox card = Ui.card(Ui.label(title, "section-title"), body, open);
            card.setId("guide-section-" + section.numero());
            page.getChildren().add(card);
            if (section.numero() == 1) page.getChildren().add(contents);
            index.getChildren().add(Ui.button(title, "secondary", () -> {
                page.applyCss(); page.layout();
                double recorrido = page.getHeight() - getViewportBounds().getHeight();
                setVvalue(recorrido <= 0 ? 0 : Math.min(1, card.getBoundsInParent().getMinY() / recorrido));
            }));
        }
        page.getChildren().add(Ui.button("Volver a Inicio", "primary", () -> navigate.accept("Inicio")));
        setContent(page);
    }
}
