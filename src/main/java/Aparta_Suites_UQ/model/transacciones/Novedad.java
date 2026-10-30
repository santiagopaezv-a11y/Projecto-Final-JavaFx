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
        this.apartamento = apartamento;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.reportado = reportado;
    }

    public Apartamento getApartamento() { return apartamento; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public Empleado getReportado() { return reportado; }
}
