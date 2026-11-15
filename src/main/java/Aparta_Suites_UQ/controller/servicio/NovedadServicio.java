package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Novedad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NovedadServicio {

    private static NovedadServicio instance;
    private NovedadServicio() {
    }
    public static NovedadServicio getInstance() {
        if (instance == null) {
            instance = new NovedadServicio();
        }
        return instance;
    }
    public Novedad registrar(Apartamento apartamento, String descripcion) {
        Novedad novedad = new Novedad(apartamento, descripcion, SesionServicio.getInstance().getEmpleadoActual());
        Empresa.getInstance().getListaNovedades().add(novedad);
        return novedad;
    }
    public List<Novedad> listarNovedades() {
        List<Novedad> lista = new ArrayList<>(Empresa.getInstance().getListaNovedades());
        Collections.reverse(lista);
        return lista;
    }
}
