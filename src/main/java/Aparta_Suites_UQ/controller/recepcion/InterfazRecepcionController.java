package Aparta_Suites_UQ.controller.recepcion;

import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.AutenticacionServicio;
import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.util.List;


public class InterfazRecepcionController {

    private final ApartamentoServicio apartamentoServicio = ApartamentoServicio.getInstance();
    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();
    private final AutenticacionServicio autenticacionServicio = AutenticacionServicio.getInstance();
    public List<Apartamento> listarApartamentos() {
        return apartamentoServicio.listarApartamentos();
    }

    public List<Reserva> listarReservas() {
        return reservaServicio.listarReservas();
    }
    public boolean autenticarAdministrador(String usuario, String password) {
        return autenticacionServicio.esAdministradorPortal(usuario, password);
    }
}
