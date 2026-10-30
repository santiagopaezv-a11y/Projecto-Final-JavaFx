package Aparta_Suites_UQ.model.transacciones;

import java.time.LocalDate;

public class Temporada {
    private TipoTemporada tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Temporada(TipoTemporada tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public TipoTemporada getTipo() { return tipo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
}
