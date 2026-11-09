package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.util.List;

public class AddReservaController {

    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();
    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();
    public Reserva getReservaEnEdicion() {
        return reservaServicio.getReservaEditar();
    }

    public boolean esEdicion() {
        return getReservaEnEdicion() != null;
    }

    public List<Apartamento> listarApartamentosDisponibles() {
        return apartamentoServicio.listarDisponibles(getReservaEnEdicion());
    }

    public void validarDatos(DatosReserva datos) {
        reservaServicio.validarDatos(datos);
    }

    public void actualizarReserva(DatosReserva datos) {
        reservaServicio.actualizarReserva(datos);
    }

    public void validarDisponibilidad(DatosReserva datos) {
        reservaServicio.validarDisponibilidad(datos);
    }

    public int parseCantidadOcupantes(String texto) {
        return reservaServicio.parseCantidadOcupantes(texto);
    }

    public Reserva crearReserva(DatosReserva datos, List<Ocupante> ocupantes) {
        return reservaServicio.crearReservaDirecta(datos, ocupantes);
    }
}
