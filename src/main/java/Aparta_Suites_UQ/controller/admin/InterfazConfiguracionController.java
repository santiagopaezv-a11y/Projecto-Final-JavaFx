package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.ConfiguracionServicio;
import Aparta_Suites_UQ.model.reserva.PoliticaCancelacion;

/**
 * Controller de la pantalla «Configuración» (política de cancelación y datos del alojamiento). Su vista es InterfazConfiguracionViewController.
 */
public class InterfazConfiguracionController {

    private final ConfiguracionServicio configuracionServicio = ConfiguracionServicio.getInstance();

    public PoliticaCancelacion getPolitica() {
        return configuracionServicio.getPolitica();
    }

    /** Solo aplica a reservas nuevas. Lanza IllegalArgumentException con el mensaje para el usuario. */
    public void guardarPolitica(String diasDevolucionTotal, String diasRetencionParcial, String porcentaje) {
        configuracionServicio.guardarPolitica(diasDevolucionTotal, diasRetencionParcial, porcentaje);
    }

    public String getNombre() {
        return configuracionServicio.getDatosAlojamiento().getNombre();
    }

    public String getNit() {
        return configuracionServicio.getDatosAlojamiento().getNit();
    }

    public String getDireccion() {
        return configuracionServicio.getDatosAlojamiento().getDireccion();
    }

    public void guardarDatos(String nombre, String nit, String direccion) {
        configuracionServicio.guardarDatosAlojamiento(nombre, nit, direccion);
    }
}
