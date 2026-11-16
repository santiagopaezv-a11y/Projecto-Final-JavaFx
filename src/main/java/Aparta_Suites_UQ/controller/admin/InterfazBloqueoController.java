package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.BloqueoServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;
import java.time.LocalDate;
import java.util.List;
public class InterfazBloqueoController {
    private final BloqueoServicio bloqueoServicio = BloqueoServicio.getInstance();
    public List<Bloqueo> listarBloqueos() {
        return bloqueoServicio.listarBloqueos();
    }
    public List<Apartamento> listarApartamentos() {
        return ApartamentoServicio.getInstance().listarApartamentos();
    }
    public void crearBloqueo(Apartamento apartamento, LocalDate inicio, LocalDate fin, String motivo) {
        bloqueoServicio.crearBloqueo(apartamento, inicio, fin, motivo);
    }
    public void eliminarBloqueo(Bloqueo bloqueo) {
        bloqueoServicio.eliminarBloqueo(bloqueo);
    }
}
