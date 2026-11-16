package Aparta_Suites_UQ.model.huesped;

import Aparta_Suites_UQ.model.interfaces.Autenticable;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Empleado extends Persona implements Autenticable, Serializable {
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
    @Override
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    @Override
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

    public static void setCantidadEmpleado(int cantidadEmpleado) {
        Empleado.cantidadEmpleado = cantidadEmpleado;
    }
}
