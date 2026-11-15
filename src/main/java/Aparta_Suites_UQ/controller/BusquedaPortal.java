package Aparta_Suites_UQ.controller;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;

import java.time.LocalDate;

/**
 * Lo último que el huésped eligió en «Búsqueda y cotización», para precargar la pantalla de reserva.
 */
public record BusquedaPortal(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
}
