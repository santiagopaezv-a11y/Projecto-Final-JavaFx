package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.utils.Navegacion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Estancia {
   private LocalDate fechaEntrada;
   private LocalDate fechaSalida;

    public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida) {
        validarFechas(fechaEntrada, fechaSalida);
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
    public void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida){
        if (fechaEntrada == null||fechaSalida == null){
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)){
            throw new IllegalArgumentException("Fecha de salida debe ser posterior a la fecha de entrada");
        }
    }
    public int calcularNoche(){
        return (int) ChronoUnit.DAYS.between(fechaEntrada,fechaSalida);
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        validarFechas(fechaEntrada, this.fechaSalida);
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        validarFechas(this.fechaEntrada, fechaSalida);
        this.fechaSalida = fechaSalida;
    }
}
