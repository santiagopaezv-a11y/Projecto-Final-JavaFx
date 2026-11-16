package Aparta_Suites_UQ.controller;

import java.util.List;
public record Informe(String titulo, List<String> columnas, List<List<String>> filas) {
}
