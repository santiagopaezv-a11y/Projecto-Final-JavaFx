package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.reserva.PoliticaCancelacion;

public class ConfiguracionServicio {
    private static ConfiguracionServicio instance;
    private ConfiguracionServicio() {
    }
    public static ConfiguracionServicio getInstance() {
        if (instance == null) {
            instance = new ConfiguracionServicio();
        }
        return instance;
    }
    public PoliticaCancelacion getPolitica() {
        return Empresa.getInstance().getPoliticaCancelacion();
    }
    public void guardarPolitica(String diasDevolucionTotal, String diasRetencionParcial, String porcentajeRetencion) {
        int diasTotal;
        int diasParcial;
        double porcentaje;
        try {
            diasTotal = Integer.parseInt(diasDevolucionTotal.trim());
            diasParcial = Integer.parseInt(diasRetencionParcial.trim());
            porcentaje = Double.parseDouble(porcentajeRetencion.trim()) / 100.0;
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Los días y el porcentaje deben ser números válidos.");
        }
        Empresa.getInstance().setPoliticaCancelacion(new PoliticaCancelacion(diasTotal, diasParcial, porcentaje));
    }
    public Empresa getDatosAlojamiento() {
        return Empresa.getInstance();
    }
    public void guardarDatosAlojamiento(String nombre, String nit, String direccion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del alojamiento no puede estar vacío.");
        }
        Empresa empresa = Empresa.getInstance();
        empresa.setNombre(nombre.trim());
        empresa.setNit(nit == null ? "" : nit.trim());
        empresa.setDireccion(direccion == null ? "" : direccion.trim());
    }
}
