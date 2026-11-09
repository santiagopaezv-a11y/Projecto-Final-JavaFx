package Aparta_Suites_UQ.controller.recepcion;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import java.util.List;

public class InterfazApartamentoController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public List<Apartamento> listarApartamentos() {
        return apartamentoServicio.listarApartamentos();
    }
}
