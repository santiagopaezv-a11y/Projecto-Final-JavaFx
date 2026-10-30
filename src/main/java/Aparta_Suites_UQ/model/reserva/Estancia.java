package Aparta_Suites_UQ.model.reserva;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Estancia {
    LocalDate fechaEntrada;
    LocalDate fechaSalida;

    public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaSalida.isBefore(fechaEntrada)||fechaEntrada.isEqual(fechaSalida)){
            throw new IllegalArgumentException("Fecha de salida debe ser posterior a la fecha de entrada");
        }
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
    public double calcularNoche(){
        return ChronoUnit.DAYS.between(fechaEntrada,fechaSalida);
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
