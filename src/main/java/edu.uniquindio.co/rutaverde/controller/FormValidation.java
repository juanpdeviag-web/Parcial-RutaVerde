package edu.uniquindio.co.rutaverde.controller;

import java.util.*;

public final class FormValidation {
    private FormValidation() {}
    public static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo + " es obligatorio.");
        return valor.trim();
    }
    public static String email(String valor) {
        String email = texto(valor, "Correo");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Escribe un correo válido.");
        return email;
    }
    public static long precio(String valor) {
        try {
            long precio = Long.parseLong(texto(valor, "Precio"));
            if (precio <= 0) throw new NumberFormatException();
            return precio;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El precio debe ser un entero positivo en COP.");
        }
    }
    public static int cupos(String valor) {
        try {
            int cupos = Integer.parseInt(texto(valor, "Cupos"));
            if (cupos < 0) throw new NumberFormatException();
            return cupos;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Los cupos deben ser un entero mayor o igual a cero.");
        }
    }
    public static List<String> lista(String valor) {
        if (valor == null || valor.isBlank()) return new ArrayList<>();
        return new ArrayList<>(Arrays.stream(valor.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().toList());
    }
}
