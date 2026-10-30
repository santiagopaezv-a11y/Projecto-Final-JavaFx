package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;
import java.time.Period;

public class Huesped extends Persona{
    String imagenOpcional;
    public Huesped(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
    }

    public String getImagenOpcional() {
        return imagenOpcional;
    }

    public void setImagenOpcional(String imagenOpcional) {
        this.imagenOpcional = imagenOpcional;
    }
}
