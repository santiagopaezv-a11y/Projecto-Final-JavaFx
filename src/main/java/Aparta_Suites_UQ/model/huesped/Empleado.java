package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Empleado extends Persona{
    String usuario;
    String contrasena;
    public Empleado(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
    }
}
