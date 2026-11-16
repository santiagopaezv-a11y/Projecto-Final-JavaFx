package Aparta_Suites_UQ.model.transacciones;

import java.io.Serializable;
import java.time.LocalDateTime;
public class Cargo implements Serializable {
    private final String concepto;
    private final double valor;
    private final LocalDateTime fecha;
    public Cargo(String concepto, double valor) {
        if (concepto == null || concepto.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del cargo no puede estar vacía.");
        }
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