package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.BusquedaPortal;
import Aparta_Suites_UQ.controller.servicio.OcupanteServicio;
import Aparta_Suites_UQ.controller.servicio.PortalServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller de la pantalla «Reserva» del huésped (perfil y reserva por el canal Portal). Su vista es InterfazReservaViewController.
 */
public class InterfazReservaController {

    private final PortalServicio portalServicio = PortalServicio.getInstance();
    public Huesped getHuespedActual() {
        return portalServicio.getHuespedActual();
    }
    public Huesped identificarPorDocumento(String documento) {
        return portalServicio.identificarPorDocumento(documento);
    }
    public Huesped guardarPerfil(String nombre, String documento, LocalDate nacimiento, String telefono, String correo) {
        return portalServicio.guardarPerfil(nombre, documento, nacimiento, telefono, correo);
    }
    public void cerrarPerfil() {
        portalServicio.cerrarPerfil();
    }
    public BusquedaPortal getUltimaBusqueda() {
        return portalServicio.getUltimaBusqueda();
    }
    public void limpiarUltimaBusqueda() {
        portalServicio.setUltimaBusqueda(null);
    }
    public List<Apartamento> buscarDisponibles(LocalDate entrada, LocalDate salida, int personas) {
        return portalServicio.buscarDisponibles(entrada, salida, personas);
    }
    public Ocupante crearOcupante(String nombre, LocalDate nacimiento) {
        return OcupanteServicio.getInstance().crearOcupante(nombre, nacimiento);
    }
    public Reserva crearReserva(Apartamento apartamento, LocalDate entrada, LocalDate salida, List<Ocupante> ocupantes) {
        return portalServicio.crearReservaPortal(apartamento, entrada, salida, ocupantes);
    }
}
