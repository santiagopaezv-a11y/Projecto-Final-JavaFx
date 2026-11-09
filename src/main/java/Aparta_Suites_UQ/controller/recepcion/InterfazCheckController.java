package Aparta_Suites_UQ.controller.recepcion;

import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.util.List;


public class InterfazCheckController {

    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();


    public List<Reserva> refrescarReservas() {
        return reservaServicio.refrescarEstados();
    }

    public List<Reserva> filtrarReservas(String texto, String filtroEstado) {
        return reservaServicio.filtrarReservas(texto, filtroEstado);
    }

    public boolean puedeCheckIn(Reserva reserva) {
        return reservaServicio.puedeCheckIn(reserva);
    }

    public boolean puedeCheckOut(Reserva reserva) {
        return reservaServicio.puedeCheckOut(reserva);
    }

    public boolean puedeDeclararNoShow(Reserva reserva) {
        return reservaServicio.puedeDeclararNoShow(reserva);
    }


    public boolean realizarCheckIn(Reserva reserva) {
        return reservaServicio.realizarCheckIn(reserva);
    }


    public void realizarCheckOut(Reserva reserva) {
        reservaServicio.realizarCheckOut(reserva);
    }

    public void declararNoShow(Reserva reserva) {
        reservaServicio.declararNoShow(reserva);
    }

    public void seleccionarReservaParaFolio(Reserva reserva) {
        reservaServicio.setReservaEditar(reserva);
    }
}
