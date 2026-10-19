package edu.uniquindio.co.rutaverde.model.services;

public class PasarelaPagoAdapter {
    private PagoAndinoAPI pagoAndinoAPI;

    public PasarelaPagoAdapter(PagoAndinoAPI pagoAndinoAPI) {
        this.pagoAndinoAPI = pagoAndinoAPI;
    }

    public int procesarPago(String numeroTarjeta, long valorCentavos, String referencia) {
        if (valorCentavos > 300000000) {
            return pagoAndinoAPI.procesarCobro(numeroTarjeta, valorCentavos, referencia);
        }
        return pagoAndinoAPI.procesarCobro(numeroTarjeta, valorCentavos, referencia);
    }

    public String obtenerAutorizacion() {
        return pagoAndinoAPI.ultimaAutorizacion();
    }
}
