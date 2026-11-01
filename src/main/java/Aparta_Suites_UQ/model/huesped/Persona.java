package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona {
    private String nombre;
    private String documento;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;

    public Persona(String nombre, String documento, LocalDate fechaNacimiento, String telefono, String correo) {
       if (nombre == null || documento == null || fechaNacimiento == null || telefono == null || correo == null) {
           throw  new IllegalArgumentException("Los datos de la persona no pueden estar vacios. ");
       }
        this.nombre = nombre;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
    }
    public int calcularEdad(LocalDate fecha) {
        if (fecha == null){
            return 0;
        }
        return Period.between(fechaNacimiento, fecha).getYears();
    }
    public int getEdadActual(){
        return calcularEdad(LocalDate.now());
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
