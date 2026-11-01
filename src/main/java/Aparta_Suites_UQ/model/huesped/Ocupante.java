package Aparta_Suites_UQ.model.huesped;

import java.time.LocalDate;
import java.time.Period;

public class Ocupante {
   private String nombre;
   private LocalDate fechaNacimiento;

    public Ocupante(String nombre, LocalDate fechaNacimiento) {
        if (nombre == null || fechaNacimiento == null) {
            throw new IllegalArgumentException("El nombre y la fecha no pueden estar vacios. ");
        }
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
    }
    public int calcularEdad(LocalDate fecha) {
        if (fecha == null || fechaNacimiento == null) {
            return 0;
        }
        if (fecha.isBefore(this.fechaNacimiento)) {
            return 0;
        }
        return Period.between(fechaNacimiento, fecha).getYears();
    }
    public boolean pagaTarifa(LocalDate fecha) {
        return calcularEdad(fecha) >= 6;
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
}
