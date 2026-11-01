package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDateTime;

public class Cargo {
    private final String descripcion;
    private final int valor;
    private final LocalDateTime fecha;

    public Cargo(String descripcion, int valor) {
        if (descripcion == null ||  descripcion.trim().isEmpty()){
            throw new IllegalArgumentException("La descripcion del cargo no puede estar vacia. ");
        }
        if (valor <= 0){
            throw new IllegalArgumentException("El valor del cargo debe ser mayor a 0. ");
        }

        this.descripcion = descripcion;
        this.valor = valor;
        this.fecha = LocalDateTime.now();
    }
    public String getDescripcion() { return descripcion; }
    public int getValor() { return valor; }
    public LocalDateTime getFecha() { return fecha; }
}
