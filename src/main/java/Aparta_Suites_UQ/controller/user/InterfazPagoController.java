package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.servicio.PortalServicio;
import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import java.util.List;

public class InterfazPagoController {
    private final PortalServicio portalServicio = PortalServicio.getInstance();
    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();
    public Huesped getHuespedActual() {
        return portalServicio.getHuespedActual();
    }
    public Huesped identificarPorDocumento(String documento) {
        return portalServicio.identificarPorDocumento(documento);
    }
    public List<Reserva> listarMisReservas() {
        return portalServicio.listarMisReservas();
    }
    public boolean pagar(Reserva reserva, MedioPago medio, String valor) {
        return portalServicio.pagar(reserva, medio, valor);
    }
    public String previsualizarCancelacion(Reserva reserva) {
        return reservaServicio.describirResultado(portalServicio.previsualizarCancelacion(reserva));
    }
    public String cancelar(Reserva reserva) {
        return reservaServicio.describirResultado(portalServicio.cancelar(reserva));
    }
}
