package Aparta_Suites_UQ.model.interfaces;

import java.time.LocalDate;

public interface Reservable {
    boolean estaDisponible(LocalDate fechaEntrada, LocalDate fechaSalida);

    boolean estaHabilitadoParaReservar();
}
