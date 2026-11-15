package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.servicio.PortalServicio;
import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import java.util.List;

/**
 * Controller de la pantalla «Autogestión y Pagos» del huésped. Su vista es InterfazPagoViewController.
 */
public class InterfazPagoController {

    private final PortalServicio portalServicio = PortalServicio.getInstance();
    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();

    public Huesped getHuespedActual() {
        return portalServicio.getHuespedActual();
    }

    /** @return el perfil con ese documento o null si no existe */
    public Huesped identificarPorDocumento(String documento) {
        return portalServicio.identificarPorDocumento(documento);
    }

    public List<Reserva> listarMisReservas() {
        return portalServicio.listarMisReservas();
    }

    /** @return true si con este pago la reserva quedó Confirmada. Lanza IllegalArgumentException. */
    public boolean pagar(Reserva reserva, MedioPago medio, String valor) {
        return portalServicio.pagar(reserva, medio, valor);
    }

    /** Texto con cuánto se le devolvería si cancelara hoy. Lanza IllegalArgumentException si no se puede cancelar. */
    public String previsualizarCancelacion(Reserva reserva) {
        return reservaServicio.describirResultado(portalServicio.previsualizarCancelacion(reserva));
    }

    /** Cancela y devuelve el texto con el resultado de la devolución. */
    public String cancelar(Reserva reserva) {
        return reservaServicio.describirResultado(portalServicio.cancelar(reserva));
    }
}
