package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.huesped.Ocupante;

import java.time.LocalDate;

public class OcupanteServicio {

    private static OcupanteServicio instance;

    private OcupanteServicio() {
    }
    public static OcupanteServicio getInstance() {
        if (instance == null) {
            instance = new OcupanteServicio();
        }
        return instance;
    }
    public Ocupante crearOcupante(String nombre, LocalDate fechaNacimiento) {
        String nombreLimpio = nombre != null ? nombre.trim() : "";
        if (nombreLimpio.isEmpty() || fechaNacimiento == null) {
            throw new IllegalArgumentException("Por favor ingrese el nombre y la fecha de nacimiento del ocupante.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
        }
        return new Ocupante(nombreLimpio, fechaNacimiento);
    }
}
