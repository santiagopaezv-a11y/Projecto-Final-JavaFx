package Aparta_Suites_UQ.model.transacciones;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;

import java.time.LocalDate;

public class Bloqueo {
    private Apartamento apartamento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;

    public Bloqueo(Apartamento apartamento, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        if (apartamento == null) {
            throw new IllegalArgumentException("El apartamento no puede estar vacio.");
        }
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo no puede estar vacio.");
        }
        validarFechas(fechaInicio, fechaFin);
        this.apartamento = apartamento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    public void validarFechas(LocalDate fechaInicio, LocalDate fechaFin){
        if (fechaInicio == null||fechaFin == null){
            throw new IllegalArgumentException("Las fechas de bloqueo no pueden ser nulas.");
        }
        if (fechaFin.isBefore(fechaInicio)){
            throw new IllegalArgumentException("La fecha fin no debe ser anterior a la fecha de inicio. ");
        }
    }
    public Apartamento getApartamento() { return apartamento; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getMotivo() { return motivo; }
}
