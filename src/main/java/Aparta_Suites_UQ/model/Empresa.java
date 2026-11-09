package Aparta_Suites_UQ.model;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.reserva.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nombre;
    private String nit;
    private String direccion;
    private static Empresa instance;
    private final List<Apartamento> listaApartamentos = new ArrayList<>();
    private final List<Reserva> listaReservas = new ArrayList<>();
    private final List<Huesped> listaHuespedes = new ArrayList<>();
    private final List<Temporada> listaTemporadas = new ArrayList<>();
    private final List<Empleado> listaEmpleados= new ArrayList<>();
    public  Empresa(String nombre, String nit, String direccion){
        this.nombre=nombre;
        this.nit=nit;
        this.direccion=direccion;
    }
    private Empresa() {
        cargarTemporadasPorDefecto();
    }
    private void cargarTemporadasPorDefecto() {
        // 1. TEMPORADA ALTA (Diciembre - Enero)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 1, 31)
        ));

        // TEMPORADA ALTA (Semana Santa 2026)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 3, 29),
                LocalDate.of(2026, 4, 5)
        ));

        // TEMPORADA ALTA (Mitad de Año)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 6, 15),
                LocalDate.of(2026, 7, 15)
        ));

        // 2. TEMPORADA MEDIA (Ejemplo: Puente Festivo)
        listaTemporadas.add(new Temporada(
                TipoTemporada.MEDIA,
                LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 5, 18)
        ));
    }
    public static Empresa getInstance() {
        if (instance == null) {
            instance = new Empresa();
        }
        return instance;
    }

    public List<Apartamento> getListaApartamentos() {
        return listaApartamentos;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public List<Huesped> getListaHuespedes(){
        return listaHuespedes;
    }

    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada != null) {
            this.listaTemporadas.add(temporada);
        }
    }
    public static boolean validarCredencialEmpleado(String user, String contrasena){
        boolean validar=false;
        for (Empleado empleado : getInstance().getListaEmpleados()){
            if (empleado.getUsuario().equals(user)&&empleado.getContrasena().equals(contrasena)){
                validar=true;
            }
        }
        return validar;
    }
}