package Aparta_Suites_UQ.model.huesped;

import Aparta_Suites_UQ.utils.Navegacion;

import java.time.LocalDate;
import java.time.Period;

public class Ocupante {
   private String nombre;
   private LocalDate fechaNacimiento;

    public Ocupante(String nombre, LocalDate fechaNacimiento) {
        if (nombre == null || nombre.trim().isEmpty()) {
           throw new IllegalArgumentException("Nombre no puede estar vacio. ");
       }
        if ( fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacia. ");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura. ");
        }

        this.nombre = nombre.trim();
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
    // GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacio. ");
        }
        this.nombre = nombre.trim();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if ( fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacia. ");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura. ");
        }
        this.fechaNacimiento = fechaNacimiento;
    }
}
