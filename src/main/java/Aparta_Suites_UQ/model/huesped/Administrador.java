package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Administrador extends Empleado{
    public Administrador(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
    }
}
