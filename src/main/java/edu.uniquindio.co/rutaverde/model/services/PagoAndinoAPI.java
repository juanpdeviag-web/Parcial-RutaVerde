package edu.uniquindio.co.rutaverde.model.services;

public class PagoAndinoAPI {
    private int consecutivo;

    public PagoAndinoAPI() {
        this.consecutivo = 1000;
    }

    public int procesarCobro(String numeroTarjeta, long valorCentavos, String referencia) {
        consecutivo++;
        if (valorCentavos <= 300000000) {
            return 0;
        } else {
            return 51;
        }
    }

    public String ultimaAutorizacion() {
        return "PA-" + consecutivo;
    }
}
