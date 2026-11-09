package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.OcupanteServicio;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import java.time.LocalDate;

public class RegistrarOcupanteController {

    private final OcupanteServicio ocupanteServicio = OcupanteServicio.getInstance();

    public Ocupante crearOcupante(String nombre, LocalDate fechaNacimiento) {
        return ocupanteServicio.crearOcupante(nombre, fechaNacimiento);
    }
}
