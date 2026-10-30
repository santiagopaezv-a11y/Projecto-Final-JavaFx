package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDateTime;

public class Cargo {
    private String descripcion;
    private int valor;
    private LocalDateTime fecha;

    public Cargo(String descripcion, int valor) {
        this.descripcion = descripcion;
        this.valor = valor;
        this.fecha = LocalDateTime.now();
    }
    public String getDescripcion() { return descripcion; }
    public int getValor() { return valor; }
    public LocalDateTime getFecha() { return fecha; }
}
