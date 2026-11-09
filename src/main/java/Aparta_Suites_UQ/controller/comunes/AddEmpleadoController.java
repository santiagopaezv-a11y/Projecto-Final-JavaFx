package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.servicio.EmpleadoServicio;
import Aparta_Suites_UQ.model.huesped.Empleado;
import java.time.LocalDate;

public class AddEmpleadoController {

    private final EmpleadoServicio empleadoServicio = EmpleadoServicio.getInstance();
    public Empleado getEmpleadoEnEdicion() {
        return empleadoServicio.getEmpleadoEditar();
    }
    public boolean guardarEmpleado(String documento, String nombre, String rol, LocalDate fechaNacimiento,
                                   String telefono, String correo, String password) {
        return empleadoServicio.guardarEmpleado(documento, nombre, rol, fechaNacimiento, telefono, correo, password);
    }

    public void cancelarEdicion() {
        empleadoServicio.setEmpleadoEditar(null);
    }
}
