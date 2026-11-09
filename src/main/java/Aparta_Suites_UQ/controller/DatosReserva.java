package Aparta_Suites_UQ.controller;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import java.time.LocalDate;
public record DatosReserva(String nombre, String documento, String telefono, String correo,
                           LocalDate fechaNacimiento, LocalDate fechaEntrada, LocalDate fechaSalida,
                           Apartamento apartamento, String cantidadOcupantes, String anticipo,
                           EstadoReserva estado) {
}
