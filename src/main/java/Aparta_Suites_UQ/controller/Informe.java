package Aparta_Suites_UQ.controller;

import java.util.List;

/**
 * Resultado tabular de un reporte: columnas y filas ya formateadas como texto.
 */
public record Informe(String titulo, List<String> columnas, List<List<String>> filas) {
}
