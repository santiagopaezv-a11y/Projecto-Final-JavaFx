package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Estancia {
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida) {
        validarFechas(fechaEntrada, fechaSalida);
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada (mínimo 1 noche).");
        }
    }

    public long getNoches() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    public LocalDateTime getFechaHoraEntrada() {
        return this.fechaEntrada.atTime(Reserva.HORA_CHECK_IN);
    }

    public LocalDateTime getFechaHoraSalida() {
        return this.fechaSalida.atTime(Reserva.HORA_CHECK_OUT);
    }

    public int calcularPersonasQuePagan(List<Ocupante> ocupantes) {
        int personasQuePagan = 1; // Incluye al titular
        if (ocupantes != null) {
            for (Ocupante ocupante : ocupantes) {
                if (ocupante.pagaTarifa(this.fechaEntrada)) {
                    personasQuePagan++;
                }
            }
        }
        return personasQuePagan;
    }

    public int calcularValorTotal(Apartamento apartamento, List<Ocupante> ocupantes, List<Temporada> temporadas) {
        if (apartamento == null) {
            throw new IllegalArgumentException("El apartamento no puede ser nulo.");
        }

        int personasQuePagan = calcularPersonasQuePagan(ocupantes);
        int valorTotal = 0;

        LocalDate fechaNoche = this.fechaEntrada;
        while (fechaNoche.isBefore(fechaSalida)) {
            int tarifaNoche = obtenerTarifaPorNoche(apartamento, fechaNoche, temporadas);
            valorTotal += (tarifaNoche * personasQuePagan);
            fechaNoche = fechaNoche.plusDays(1);
        }
        return valorTotal;
    }

    private int obtenerTarifaPorNoche(Apartamento apartamento, LocalDate fechaNoche, List<Temporada> temporadas) {
        if (temporadas != null) {
            for (Temporada temporada : temporadas) {
                if (temporada.aplicaEnFecha(fechaNoche)) {
                    return apartamento.getTarifaSegunTemporada(temporada.getTipo());
                }
            }
        }
        return apartamento.getTarifaSegunTemporada(TipoTemporada.BAJA);
    }

    public boolean seSolapaCon(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            return false;
        }
        return inicio.isBefore(this.fechaSalida) && fin.isAfter(this.fechaEntrada);
    }

    public static boolean seSolapan(LocalDate inicioA, LocalDate finA, LocalDate inicioB, LocalDate finB) {
        if (inicioA == null || finA == null || inicioB == null || finB == null) {
            return false;
        }
        return inicioA.isBefore(finB) && finA.isAfter(inicioB);
    }

    // Getters y Setters
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