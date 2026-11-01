package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Huesped extends Persona {

    private String imagenOpcional;

    public Huesped(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo, String imagenOpcional) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
        this.imagenOpcional = imagenOpcional;
    }

    public Huesped(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo) {
        this(nombre, documento, fechaNacimiento, telefono, correo, null);
    }

    public boolean pagaTarifa(LocalDate fechaEntrada){
        return calcularEdad(fechaEntrada) >= 6;
    }
    public String getImagenOpcional() {
        return imagenOpcional;
    }

    public void setImagenOpcional(String imagenOpcional) {
        this.imagenOpcional = imagenOpcional;
    }
}


