package Aparta_Suites_UQ.controller;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.NocheLiquidada;
import java.time.LocalDate;
import java.util.List;
public record Cotizacion(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas,
                         int personasQuePagan, List<NocheLiquidada> noches, int total, int anticipoPortal) {
}
