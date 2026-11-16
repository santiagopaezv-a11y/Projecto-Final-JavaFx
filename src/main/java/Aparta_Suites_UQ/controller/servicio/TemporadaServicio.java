package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class TemporadaServicio {
    private static TemporadaServicio instance;
    private TemporadaServicio() {
    }
    public static TemporadaServicio getInstance() {
        if (instance == null) {
            instance = new TemporadaServicio();
        }
        return instance;
    }
    public List<Temporada> listarTemporadas() {
        return Empresa.getInstance().getListaTemporadas().stream()
                .sorted(Comparator.comparing(Temporada::getFechaInicio))
                .collect(Collectors.toList());
    }
    public Temporada crearTemporada(TipoTemporada tipo, LocalDate inicio, LocalDate fin) {
        validar(tipo, inicio, fin, null);
        Temporada temporada = new Temporada(tipo, inicio, fin);
        Empresa.getInstance().agregarTemporada(temporada);
        return temporada;
    }
    public void actualizarTemporada(Temporada temporada, TipoTemporada tipo, LocalDate inicio, LocalDate fin) {
        if (temporada == null) {
            throw new IllegalArgumentException("Seleccione una temporada de la tabla.");
        }
        validar(tipo, inicio, fin, temporada);
        temporada.setRangoFechas(inicio, fin);
        temporada.setTipo(tipo);
    }
    public void eliminarTemporada(Temporada temporada) {
        if (temporada == null) {
            throw new IllegalArgumentException("Seleccione una temporada de la tabla.");
        }
        Empresa.getInstance().getListaTemporadas().remove(temporada);
    }
    private void validar(TipoTemporada tipo, LocalDate inicio, LocalDate fin, Temporada excluida) {
        if (tipo == null || inicio == null || fin == null) {
            throw new IllegalArgumentException("Complete el tipo y las fechas de la temporada.");
        }
        if (tipo == TipoTemporada.BAJA) {
            throw new IllegalArgumentException(
                    "La temporada Baja es la base: cubre todas las fechas que no pertenezcan a otra temporada.");
        }
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha de inicio.");
        }
        for (Temporada otra : Empresa.getInstance().getListaTemporadas()) {
            if (otra != excluida && !fin.isBefore(otra.getFechaInicio()) && !inicio.isAfter(otra.getFechaFin())) {
                throw new IllegalArgumentException("Las fechas se cruzan con la temporada " + otra.getTipo()
                        + " (" + otra.getFechaInicio() + " a " + otra.getFechaFin() + ").");
            }
        }
    }
    public void guardarTarifas(Apartamento apartamento, String baja, String media, String alta) {
        if (apartamento == null) {
            throw new IllegalArgumentException("Seleccione un apartamento.");
        }
        double tarifaBaja = parsear(baja, "Baja");
        double tarifaMedia = parsear(media, "Media");
        double tarifaAlta = parsear(alta, "Alta");
        apartamento.setPrecioNoche(tarifaBaja);
        apartamento.setTarifa(TipoTemporada.MEDIA, tarifaMedia);
        apartamento.setTarifa(TipoTemporada.ALTA, tarifaAlta);
    }
    private double parsear(String texto, String temporada) {
        try {
            double valor = Double.parseDouble(texto.trim());
            if (valor <= 0) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("La tarifa " + temporada + " debe ser un número mayor a cero.");
        }
    }
}
