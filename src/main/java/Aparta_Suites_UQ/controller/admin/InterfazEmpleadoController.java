package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.EmpleadoServicio;
import Aparta_Suites_UQ.model.huesped.Empleado;
import java.util.List;

public class InterfazEmpleadoController {

    private final EmpleadoServicio empleadoServicio = EmpleadoServicio.getInstance();

    public List<Empleado> listarEmpleados() {
        return empleadoServicio.listarEmpleados();
    }
    public boolean coincideFiltro(Empleado empleado, String texto, String rol) {
        return empleadoServicio.coincideFiltro(empleado, texto, rol);
    }
    public void prepararNuevoEmpleado() {
        empleadoServicio.setEmpleadoEditar(null);
    }

    public void prepararEdicion(Empleado empleado) {
        empleadoServicio.setEmpleadoEditar(empleado);
    }

    public void eliminarEmpleado(Empleado empleado) {
        empleadoServicio.eliminarEmpleado(empleado);
    }
}
