package edu.uniquindio.co.rutaverde.model.entities;

import java.util.Objects;

/** Número de tarjeta conservado únicamente durante la sesión. */
public final class Tarjeta {
    private final String numero;

    public Tarjeta(String numero) { this.numero = Objects.requireNonNull(numero); }

    public String getNumero() { return numero; }

    @Override
    public String toString() {
        return "•••• " + numero.substring(Math.max(0, numero.length() - 4));
    }
}
