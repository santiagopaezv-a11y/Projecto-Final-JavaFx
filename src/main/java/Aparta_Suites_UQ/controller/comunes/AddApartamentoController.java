package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;


public class AddApartamentoController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();

    public Apartamento getApartamentoEnEdicion() {
        return apartamentoServicio.getApartamentoSeleccionado();
    }
    public void guardarApartamento(String nombre, String dormitorios, String capacidad, String caracteristicas,
                                   String precioNoche, String estado, String motivo) {
        apartamentoServicio.guardarApartamento(nombre, dormitorios, capacidad, caracteristicas,
                precioNoche, estado, motivo);
    }
}
