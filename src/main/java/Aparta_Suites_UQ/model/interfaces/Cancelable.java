package Aparta_Suites_UQ.model.interfaces;

import Aparta_Suites_UQ.model.reserva.ResultadoCancelacion;

public interface Cancelable {
    boolean puedeCancelarse();
    ResultadoCancelacion calcularResultadoCancelacion();
    void cancelar();
}
