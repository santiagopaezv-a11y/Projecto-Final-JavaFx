package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import java.util.List;

public class ApartamentoPanelController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public List<Apartamento> listarApartamentos() {
        return apartamentoServicio.listarApartamentos();
    }
}
