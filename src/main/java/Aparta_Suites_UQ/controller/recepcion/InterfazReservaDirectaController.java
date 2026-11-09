package Aparta_Suites_UQ.controller.recepcion;

import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.util.List;

public class InterfazReservaDirectaController {

    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();

    public List<Reserva> listarReservas() {
        return reservaServicio.listarReservas();
    }
    public void iniciarEdicion(Reserva reserva) {
        reservaServicio.setReservaEditar(reserva);
    }
    public void finalizarEdicion() {
        reservaServicio.setReservaEditar(null);
    }
    public void eliminarReserva(Reserva reserva) {
        reservaServicio.eliminarReserva(reserva);
    }
}
