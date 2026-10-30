package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;
import java.time.Period;

public class Ocupante {
    String nombre;
    LocalDate fechaNacimiento;

    public Ocupante(String nombre, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    int calcularEdad(){
        Period periodo= Period.between(getFechaNacimiento(),LocalDate.now());
        return periodo.getYears();
    }
}
