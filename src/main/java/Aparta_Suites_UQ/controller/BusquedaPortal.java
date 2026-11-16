package Aparta_Suites_UQ.controller;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import java.time.LocalDate;
public record BusquedaPortal(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
}
