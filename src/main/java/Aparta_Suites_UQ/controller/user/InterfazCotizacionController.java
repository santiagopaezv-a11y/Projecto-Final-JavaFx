package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.BusquedaPortal;
import Aparta_Suites_UQ.controller.Cotizacion;
import Aparta_Suites_UQ.controller.servicio.PortalServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import java.time.LocalDate;
import java.util.List;

public class InterfazCotizacionController {
    private final PortalServicio portalServicio = PortalServicio.getInstance();
    public List<Apartamento> buscarDisponibles(LocalDate entrada, LocalDate salida, int personas) {
        return portalServicio.buscarDisponibles(entrada, salida, personas);
    }
    public Cotizacion cotizar(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
        return portalServicio.cotizar(apartamento, entrada, salida, personas, menores);
    }
    public void guardarSeleccion(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
        portalServicio.setUltimaBusqueda(new BusquedaPortal(apartamento, entrada, salida, personas, menores));
    }
}
