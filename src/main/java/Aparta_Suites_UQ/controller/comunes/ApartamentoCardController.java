package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;


public class ApartamentoCardController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public boolean eliminarApartamento(Apartamento apartamento) {
        return apartamentoServicio.eliminarApartamento(apartamento);
    }

    public void seleccionarParaEdicion(Apartamento apartamento) {
        apartamentoServicio.setApartamentoSeleccionado(apartamento);
    }
}
