package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;


public class ApartamentoFueraServicioController {

    public void asignarMotivo(Apartamento apartamento, String motivo) {
        apartamento.ponerFueraDeServicio(motivo);
    }
}
