package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;

public class EditarEstadoApartamentoController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public Apartamento getApartamentoEnEdicion() {
        return apartamentoServicio.getApartamentoSeleccionado();
    }

    public void guardarEstado(EstadoOperativo estado) {
        apartamentoServicio.actualizarEstadoOperativo(estado);
    }

    public void cancelarEdicion() {
        apartamentoServicio.setApartamentoSeleccionado(null);
    }
}
