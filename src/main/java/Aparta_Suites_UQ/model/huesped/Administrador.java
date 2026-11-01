package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Administrador extends Empleado{
    public Administrador(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo, String contrasena, String usuario) {
        super(nombre, documento, fechaNacimiento, telefono, correo, contrasena, usuario);
    }
}
