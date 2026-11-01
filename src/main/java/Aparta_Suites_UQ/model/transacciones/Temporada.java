package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDate;

public class Temporada {
    private TipoTemporada tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Temporada(TipoTemporada tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de temporada no puede ser nulo");
        }
        validarFechas(fechaInicio, fechaFin);

        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fechas no pueden ser nulas");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha de inicio");
        }
    }

    public boolean aplicaEnFecha(LocalDate fecha){
        if (fecha == null) return false;

        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }


    //GETTERS Y SETTERS

    public TipoTemporada getTipo() { return tipo; }
    public void setTipo(TipoTemporada tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de temporada no puede ser nulo. ");
        }
        this.tipo = tipo;
    }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    public void setRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}
