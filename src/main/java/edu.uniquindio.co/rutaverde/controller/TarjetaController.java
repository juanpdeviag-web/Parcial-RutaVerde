package edu.uniquindio.co.rutaverde.controller;

import edu.uniquindio.co.rutaverde.model.entities.Tarjeta;
import java.util.List;

public class TarjetaController {
    public static final String NUMERO_DEMO = "4539148803436467";
    private final MainController app;

    public TarjetaController(MainController app) { this.app = app; }

    public List<Tarjeta> listar() { return app.tarjetas.listar(); }

    public Tarjeta agregar(String numero) {
        if (!app.validador.validar(numero) || !app.validador.validarLongitud(numero)
                || !app.validador.validarSoloDigitos(numero))
            throw new IllegalArgumentException("El número de tarjeta no es válido. Revisa sus dígitos.");
        String normalizado = numero.replaceAll("\\s", "");
        if (app.tarjetas.buscar(normalizado) != null)
            throw new IllegalArgumentException("Esta tarjeta ya está registrada. Selecciónala en Tarjeta registrada.");
        Tarjeta tarjeta = new Tarjeta(normalizado);
        app.tarjetas.guardar(tarjeta);
        return tarjeta;
    }

    public String etiqueta(Tarjeta tarjeta) {
        return (NUMERO_DEMO.equals(tarjeta.getNumero()) ? "Demo · " : "Tarjeta · ") + tarjeta;
    }
}
