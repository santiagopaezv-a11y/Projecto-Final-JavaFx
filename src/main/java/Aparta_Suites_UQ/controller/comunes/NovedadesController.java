package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.NovedadServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Novedad;
import java.util.List;

/**
 * Controller de la ventana «Novedades» (daños, faltantes o situaciones reportadas en un apartamento). Su vista es NovedadesViewController.
 */
public class NovedadesController {

    public List<Apartamento> listarApartamentos() {
        return ApartamentoServicio.getInstance().listarApartamentos();
    }

    public List<Novedad> listarNovedades() {
        return NovedadServicio.getInstance().listarNovedades();
    }

    /** La registra el empleado en sesión. Lanza IllegalArgumentException si falta apartamento o descripción. */
    public void registrar(Apartamento apartamento, String descripcion) {
        NovedadServicio.getInstance().registrar(apartamento, descripcion);
    }
}
