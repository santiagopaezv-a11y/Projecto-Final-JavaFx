package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;

public abstract class Empleado extends Persona{
    private String usuario;
    private String contrasena;
    private static int cantidadEmpleado;

    public Empleado(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo, String contrasena) {
        super(nombre, documento, fechaNacimiento, telefono, correo);
        ++cantidadEmpleado;
        this.usuario=getUserNombre(nombre);
        this.contrasena=contrasena;
    }
    public abstract String getRol();
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    private String getUserNombre(String nombre){
        String ID = String.format("%03d", getCantidadEmpleado());
        String nombreReplace= nombre.replace(" ", "").toLowerCase();
        String user=nombreReplace+"_"+ID;
        return user;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public static int getCantidadEmpleado() {
        return cantidadEmpleado;
    }
}
