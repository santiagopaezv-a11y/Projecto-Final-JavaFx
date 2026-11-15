package Aparta_Suites_UQ.controller.admin;

import Aparta_Suites_UQ.controller.servicio.EmpleadoServicio;
import Aparta_Suites_UQ.model.huesped.Empleado;
import java.util.List;

/**
 * Controller de la pantalla «Gestión de empleados». Su vista es InterfazEmpleadoViewController.
 */
public class InterfazEmpleadoController {

    private final EmpleadoServicio empleadoServicio = EmpleadoServicio.getInstance();

    public List<Empleado> listarEmpleados() {
        return empleadoServicio.listarEmpleados();
    }

    /** Regla del filtro: texto en nombre/documento/correo y rol ("TODOS" = sin filtro). */
    public boolean coincideFiltro(Empleado empleado, String texto, String rol) {
        return empleadoServicio.coincideFiltro(empleado, texto, rol);
    }

    /** Deja el formulario en modo «registrar». */
    public void prepararNuevoEmpleado() {
        empleadoServicio.setEmpleadoEditar(null);
    }

    /** Deja el formulario en modo «editar» sobre el empleado elegido. */
    public void prepararEdicion(Empleado empleado) {
        empleadoServicio.setEmpleadoEditar(empleado);
    }

    public void eliminarEmpleado(Empleado empleado) {
        empleadoServicio.eliminarEmpleado(empleado);
    }
}
