package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Huesped;

public class SesionServicio {
    private static SesionServicio instance;
    private Empleado empleadoActual;
    private Huesped huespedActual;
    private SesionServicio() {
    }
    public static SesionServicio getInstance() {
        if (instance == null) {
            instance = new SesionServicio();
        }
        return instance;
    }
    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
    }
    public Empleado getEmpleadoActual() {
        return empleadoActual != null ? empleadoActual : Empresa.getInstance().getEmpleadoSistema();
    }
    public Huesped getHuespedActual() {
        return huespedActual;
    }
    public void setHuespedActual(Huesped huesped) {
        this.huespedActual = huesped;
    }
}
