package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.NovedadServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Novedad;
import java.util.List;
public class NovedadesController {
    public List<Apartamento> listarApartamentos() {
        return ApartamentoServicio.getInstance().listarApartamentos();
    }
    public List<Novedad> listarNovedades() {
        return NovedadServicio.getInstance().listarNovedades();
    }
    public void registrar(Apartamento apartamento, String descripcion) {
        NovedadServicio.getInstance().registrar(apartamento, descripcion);
    }
}
