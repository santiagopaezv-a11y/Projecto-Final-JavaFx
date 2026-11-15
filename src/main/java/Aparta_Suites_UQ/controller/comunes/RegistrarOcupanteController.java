package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.OcupanteServicio;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import java.time.LocalDate;

/**
 * Controller del modal «Registrar ocupante». Su vista es RegistrarOcupanteViewController.
 */
public class RegistrarOcupanteController {

    private final OcupanteServicio ocupanteServicio = OcupanteServicio.getInstance();

    /** Valida y crea el ocupante. Lanza IllegalArgumentException con el mensaje para el usuario. */
    public Ocupante crearOcupante(String nombre, LocalDate fechaNacimiento) {
        return ocupanteServicio.crearOcupante(nombre, fechaNacimiento);
    }
}
