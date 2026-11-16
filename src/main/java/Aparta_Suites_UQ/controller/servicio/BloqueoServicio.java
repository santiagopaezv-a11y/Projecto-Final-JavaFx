package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BloqueoServicio {
    private static BloqueoServicio instance;
    private BloqueoServicio() {
    }
    public static BloqueoServicio getInstance() {
        if (instance == null) {
            instance = new BloqueoServicio();
        }
        return instance;
    }
    public List<Bloqueo> listarBloqueos() {
        return ApartamentoServicio.getInstance().listarApartamentos().stream()
                .flatMap(a -> a.getBloqueos().stream())
                .sorted(Comparator.comparing(Bloqueo::getFechaInicio))
                .collect(Collectors.toList());
    }
    public Bloqueo crearBloqueo(Apartamento apartamento, LocalDate inicio, LocalDate fin, String motivo) {
        if (apartamento == null) {
            throw new IllegalArgumentException("Seleccione un apartamento.");
        }
        try {
            Bloqueo bloqueo = new Bloqueo(apartamento, inicio, fin, motivo == null ? "" : motivo.trim());
            if (bloqueo.coincideConFechaActual()) {
                apartamento.setEstadoOperativo(EstadoOperativo.FUERA_DE_SERVICIO);
                apartamento.setMotivoFueraServicio(bloqueo.getMotivo());
            }
            apartamento.agregarBloqueo(bloqueo);
            return bloqueo;
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public void eliminarBloqueo(Bloqueo bloqueo) {
        if (bloqueo == null) {
            throw new IllegalArgumentException("Seleccione un bloqueo de la tabla.");
        }
        bloqueo.getApartamento().eliminarBloqueo(bloqueo);
    }
}
