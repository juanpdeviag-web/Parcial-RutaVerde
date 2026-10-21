package edu.uniquindio.co.rutaverde.util;

public class LuhnValidator {
    private final CreditCardValidator creditCardValidator;

    public LuhnValidator() {
        this.creditCardValidator = new CreditCardValidator(new LuhnAlgorithm());
    }

    public boolean validar(String numeroTarjeta) {
        return creditCardValidator.validar(numeroTarjeta);
    }
}
