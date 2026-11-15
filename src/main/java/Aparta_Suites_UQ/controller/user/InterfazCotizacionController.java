package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.BusquedaPortal;
import Aparta_Suites_UQ.controller.Cotizacion;
import Aparta_Suites_UQ.controller.servicio.PortalServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller de la pantalla «Búsqueda y cotización» del huésped. Su vista es InterfazCotizacionViewController.
 */
public class InterfazCotizacionController {

    private final PortalServicio portalServicio = PortalServicio.getInstance();

    /** Apartamentos libres para el rango y el número de personas. Lanza IllegalArgumentException. */
    public List<Apartamento> buscarDisponibles(LocalDate entrada, LocalDate salida, int personas) {
        return portalServicio.buscarDisponibles(entrada, salida, personas);
    }

    /** Valor total y desglose noche por noche. Lanza IllegalArgumentException. */
    public Cotizacion cotizar(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
        return portalServicio.cotizar(apartamento, entrada, salida, personas, menores);
    }

    /** Deja la elección lista para precargar la pantalla de reserva. */
    public void guardarSeleccion(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
        portalServicio.setUltimaBusqueda(new BusquedaPortal(apartamento, entrada, salida, personas, menores));
    }
}
