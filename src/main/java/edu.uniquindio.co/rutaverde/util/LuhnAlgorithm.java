package edu.uniquindio.co.rutaverde.util;

public class LuhnAlgorithm {
    public boolean isValid(String digits) {
        if (digits == null || !digits.matches("\\d+")) {
            return false;
        }

        int suma = 0;
        boolean duplicar = false;
        
        for (int i = digits.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(digits.charAt(i));
            
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
