package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Recepcionista extends Empleado{
    public Recepcionista(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo, String usuario,  String contrasena) {
        super(nombre, documento, fechaNacimiento, telefono, correo, usuario,  contrasena);
    }
}
