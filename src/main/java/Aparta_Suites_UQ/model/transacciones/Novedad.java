package Aparta_Suites_UQ.model.transacciones;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Empleado;

import java.time.LocalDateTime;

public class Novedad {
    private Apartamento apartamento;
    private String descripcion; // Daños, faltantes o situaciones reportadas[cite: 5]
    private LocalDateTime fecha;
    private Empleado reportado;

    public Novedad(Apartamento apartamento, String descripcion, Empleado reportado) {
        if (apartamento == null) {
            throw new IllegalArgumentException("El apartamento no puede estar vacio. ");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia.");
        }
        if (reportado == null) {
            throw new IllegalArgumentException("El empleado que reporta la novedad no puede ser nulo. ");
        }
        this.apartamento = apartamento;
        this.descripcion = descripcion.trim();
        this.fecha = LocalDateTime.now();
        this.reportado = reportado;
    }

    public Apartamento getApartamento() { return apartamento; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public Empleado getReportado() { return reportado; }
}
