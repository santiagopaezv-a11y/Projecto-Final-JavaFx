package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.ConfiguracionServicio;
import Aparta_Suites_UQ.model.reserva.PoliticaCancelacion;
public class InterfazConfiguracionController {
    private final ConfiguracionServicio configuracionServicio = ConfiguracionServicio.getInstance();
    public PoliticaCancelacion getPolitica() {
        return configuracionServicio.getPolitica();
    }
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
