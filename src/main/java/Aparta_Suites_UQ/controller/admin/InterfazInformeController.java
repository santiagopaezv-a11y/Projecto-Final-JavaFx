package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.Informe;
import Aparta_Suites_UQ.controller.servicio.InformeServicio;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller de la pantalla «Informes» (reportes avanzados). Su vista es InterfazInformeViewController.
 */
public class InterfazInformeController {

    private final InformeServicio informeServicio = InformeServicio.getInstance();

    public List<String> nombresDeInformes() {
        return informeServicio.nombresDeInformes();
    }

    public boolean requierePeriodo(String nombre) {
        return informeServicio.requierePeriodo(nombre);
    }

    /** Lanza IllegalArgumentException con el mensaje para el usuario si faltan datos. */
    public Informe generar(String nombre, LocalDate desde, LocalDate hasta) {
        return informeServicio.generar(nombre, desde, hasta);
    }
}
