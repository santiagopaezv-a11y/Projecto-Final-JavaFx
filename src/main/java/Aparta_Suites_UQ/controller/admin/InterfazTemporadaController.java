package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.TemporadaServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller de la pantalla «Temporadas y tarifas» (administrador). Su vista es InterfazTemporadaViewController.
 */
public class InterfazTemporadaController {

    private final TemporadaServicio temporadaServicio = TemporadaServicio.getInstance();

    public List<Temporada> listarTemporadas() {
        return temporadaServicio.listarTemporadas();
    }

    public List<Apartamento> listarApartamentos() {
        return ApartamentoServicio.getInstance().listarApartamentos();
    }

    // Los siguientes métodos lanzan IllegalArgumentException con el mensaje para el usuario.

    public void crearTemporada(TipoTemporada tipo, LocalDate inicio, LocalDate fin) {
        temporadaServicio.crearTemporada(tipo, inicio, fin);
    }

    public void actualizarTemporada(Temporada temporada, TipoTemporada tipo, LocalDate inicio, LocalDate fin) {
        temporadaServicio.actualizarTemporada(temporada, tipo, inicio, fin);
    }

    public void eliminarTemporada(Temporada temporada) {
        temporadaServicio.eliminarTemporada(temporada);
    }

    public void guardarTarifas(Apartamento apartamento, String baja, String media, String alta) {
        temporadaServicio.guardarTarifas(apartamento, baja, media, alta);
    }
}
