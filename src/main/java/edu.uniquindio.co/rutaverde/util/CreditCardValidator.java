package edu.uniquindio.co.rutaverde.util;

public class CreditCardValidator {
    private final LuhnAlgorithm luhnAlgorithm;

    public CreditCardValidator(LuhnAlgorithm luhnAlgorithm) {
        this.luhnAlgorithm = luhnAlgorithm;
    }

    public boolean validar(String numeroTarjeta) {
        if (numeroTarjeta == null) {
            return false;
        }
        
        String numero = numeroTarjeta.replaceAll("\\s", "");
        
        if (numero.length() < 13 || numero.length() > 19) {
            return false;
        }
        
        return luhnAlgorithm.isValid(numero);
    }
}
