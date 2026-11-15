package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;

/**
 * Controller del modal «Editar estado del apartamento». Su vista es EditarEstadoApartamentoViewController.
 */
public class EditarEstadoApartamentoController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public Apartamento getApartamentoEnEdicion() {
        return apartamentoServicio.getApartamentoSeleccionado();
    }

    /** Cambia el estado operativo. Lanza IllegalArgumentException si no se eligió estado. */
    public void guardarEstado(EstadoOperativo estado) {
        apartamentoServicio.actualizarEstadoOperativo(estado);
    }

    public void cancelarEdicion() {
        apartamentoServicio.setApartamentoSeleccionado(null);
    }
}
