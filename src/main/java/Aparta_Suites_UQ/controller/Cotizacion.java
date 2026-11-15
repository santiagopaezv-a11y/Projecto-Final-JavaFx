package Aparta_Suites_UQ.controller;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.NocheLiquidada;

import java.time.LocalDate;
import java.util.List;

/**
 * Cotización de una estancia: valor total y desglose noche por noche según la temporada.
 */
public record Cotizacion(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas,
                         int personasQuePagan, List<NocheLiquidada> noches, int total, int anticipoPortal) {
}
