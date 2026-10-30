package Aparta_Suites_UQ.model.transacciones;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;

import java.time.LocalDate;

public class Bloqueo {
    private Apartamento apartamento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
    public Bloqueo(Apartamento apartamento, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.apartamento = apartamento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }
    public Apartamento getApartamento() { return apartamento; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getMotivo() { return motivo; }
}
