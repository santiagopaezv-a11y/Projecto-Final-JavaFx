package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDateTime;

public class Cargo {
    private final String concepto;
    private final double valor;
    private final LocalDateTime fecha;

    public Cargo(String concepto, double valor) {
        if (concepto == null || concepto.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del cargo no puede estar vacía.");
        }
        // Permitir valores distintos de cero (los positivos son cargos, los negativos correcciones)
        if (valor == 0) {
            throw new IllegalArgumentException("El valor del cargo no puede ser cero.");
        }

        this.concepto = concepto;
        this.valor = valor;
        this.fecha = LocalDateTime.now();
    }

    public String getConcepto() { return concepto; }
    public double getValor() { return valor; }
    public LocalDateTime getFecha() { return fecha; }
}