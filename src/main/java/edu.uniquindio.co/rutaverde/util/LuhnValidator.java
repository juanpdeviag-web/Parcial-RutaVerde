package edu.uniquindio.co.rutaverde.util;

public class LuhnValidator {
    
    public boolean validar(String numeroTarjeta) {
        if (numeroTarjeta == null) {
            return false;
        }
        
        String numero = numeroTarjeta.replaceAll("\\s", "");
        
        if (numero.length() < 13 || numero.length() > 19) {
            return false;
        }
        
        if (!numero.matches("\\d+")) {
            return false;
        }
        
        return aplicarLuhn(numero);
    }
    
    private boolean aplicarLuhn(String numero) {
        int suma = 0;
        boolean duplicar = false;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            
            if (duplicar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            
            suma += digito;
            duplicar = !duplicar;
        }
        
        return suma % 10 == 0;
    }
}
