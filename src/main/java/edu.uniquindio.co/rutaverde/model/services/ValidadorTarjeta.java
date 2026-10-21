package edu.uniquindio.co.rutaverde.model.services;

import edu.uniquindio.co.rutaverde.util.LuhnValidator;

public class ValidadorTarjeta {
    private LuhnValidator luhnValidator;

    public ValidadorTarjeta(LuhnValidator luhnValidator) {
        this.luhnValidator = luhnValidator;
    }

    public boolean validar(String numeroTarjeta) {
        return luhnValidator.validar(numeroTarjeta);
    }

    public boolean validarLongitud(String numeroTarjeta) {
        if (numeroTarjeta == null) {
            return false;
        }
        String numero = numeroTarjeta.replaceAll("\\s", "");
        return numero.length() >= 13 && numero.length() <= 19;
    }

    public boolean validarSoloDigitos(String numeroTarjeta) {
        if (numeroTarjeta == null) {
            return false;
        }
        String numero = numeroTarjeta.replaceAll("\\s", "");
        return numero.matches("\\d+");
    }
}
