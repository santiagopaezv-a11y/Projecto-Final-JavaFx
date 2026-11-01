package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public class Empleado extends Persona{
    private String usuario;
    private String contrasena;

    public Empleado(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasena) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
        this.usuario=usuario;
        this.contrasena=contrasena;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
