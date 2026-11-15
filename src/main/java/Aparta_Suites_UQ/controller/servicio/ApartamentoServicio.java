package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.utils.PersistenciaUtil;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ApartamentoServicio {

    private static ApartamentoServicio instance;
    private Apartamento apartamentoSeleccionado;

    private ApartamentoServicio() {
    }

    public static ApartamentoServicio getInstance() {
        if (instance == null) {
            instance = new ApartamentoServicio();
        }
        return instance;
    }

    public List<Apartamento> listarApartamentos() {
        return Empresa.getInstance().getListaApartamentos().stream()
                .filter(Apartamento::isActivo)
                .collect(Collectors.toList());
    }

    public List<Apartamento> buscarDisponibles(LocalDate entrada, LocalDate salida, int personas) {
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Seleccione la fecha de entrada y la de salida.");
        }
        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy.");
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada (mínimo una noche).");
        }
        if (personas <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor a cero.");
        }
        return listarApartamentos().stream()
                .filter(Apartamento::estaHabilitadoParaReservar)
                .filter(a -> a.getCantMaximaPersonas() >= personas)
                .filter(a -> a.estaDisponible(entrada, salida))
                .collect(Collectors.toList());
    }

    public List<Apartamento> listarDisponibles(Reserva reservaEnEdicion) {
        List<Apartamento> disponibles = new ArrayList<>();
        for (Apartamento apartamento : listarApartamentos()) {
            if (apartamento.getEstadoOperativo() != EstadoOperativo.FUERA_DE_SERVICIO) {
                disponibles.add(apartamento);
            }
        }
        if (reservaEnEdicion != null && reservaEnEdicion.getApartamento() != null
                && !disponibles.contains(reservaEnEdicion.getApartamento())) {
            disponibles.add(reservaEnEdicion.getApartamento());
        }
        return disponibles;
    }

    public Apartamento getApartamentoSeleccionado() {
        return apartamentoSeleccionado;
    }

    public void setApartamentoSeleccionado(Apartamento apartamento) {
        this.apartamentoSeleccionado = apartamento;
    }

    public boolean eliminarApartamento(Apartamento apartamento) {
        if (apartamento == null) {
            return false;
        }
        boolean tieneReservasActivas = apartamento.getReservas().stream().anyMatch(r -> r.getEstado().esActiva());
        if (tieneReservasActivas) {
            throw new IllegalArgumentException("No se puede eliminar " + apartamento.getNombre()
                    + " porque tiene reservas activas (Pendiente, Confirmada o En curso).");
        }
        apartamento.setActivo(false);

        // PERSISTENCIA: Guardar cambio de estado activo
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        return true;
    }

    public EstadoOperativo estadoDesdeTexto(String texto) {
        if (texto == null) {
            return EstadoOperativo.PENDIENTE;
        }
        switch (texto) {
            case "Pendiente":
                return EstadoOperativo.PENDIENTE;
            case "En preparacion":
                return EstadoOperativo.EN_PREPARACION;
            case "Ocupado":
                return EstadoOperativo.OCUPADO;
            case "Fuera de Servicio":
                return EstadoOperativo.FUERA_DE_SERVICIO;
            case "Preparado":
                return EstadoOperativo.PREPARADO;
            default:
                throw new IllegalArgumentException("Estado no válido o no seleccionado");
        }
    }

    public void guardarApartamento(String nombre, String dormitoriosTxt, String capacidadTxt,
                                   String caracteristicas, String precioNocheTxt,
                                   String estadoTxt) {
        int dormitorios;
        int capacidad;
        double precioNoche;
        try {
            dormitorios = Integer.parseInt(dormitoriosTxt.trim());
            capacidad = Integer.parseInt(capacidadTxt.trim());
            precioNoche = Double.parseDouble(precioNocheTxt.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Verifique que los campos numéricos tengan valores válidos.");
        }
        if (dormitorios < 0 || capacidad < 0 || precioNoche < 0.0) {
            throw new IllegalArgumentException("Se debe enviar valores positivos");
        }

        EstadoOperativo estado = estadoDesdeTexto(estadoTxt);
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        String caracteristicasLimpias = caracteristicas == null ? "" : caracteristicas.trim();

        if (apartamentoSeleccionado != null) {
            // Modo edición
            apartamentoSeleccionado.setNumeroDormitorios(dormitorios);
            apartamentoSeleccionado.setCantMaximaPersonas(capacidad);
            apartamentoSeleccionado.setCaracteristicas(caracteristicasLimpias);
            apartamentoSeleccionado.setPrecioNoche(precioNoche);
            apartamentoSeleccionado.setEstadoOperativo(estado);
            apartamentoSeleccionado = null;
        } else {
            // Modo creación
            Apartamento nuevo = new Apartamento(nombreLimpio, dormitorios, capacidad,
                    caracteristicasLimpias, precioNoche, estado);
            Empresa.getInstance().getListaApartamentos().add(nuevo);
        }

        // PERSISTENCIA: Guardar estado actualizado en el archivo
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
    }

    public void actualizarEstadoOperativo(EstadoOperativo estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Por favor seleccione un estado operativo.");
        }
        if (apartamentoSeleccionado != null) {
            apartamentoSeleccionado.setEstadoOperativo(estado);
            apartamentoSeleccionado = null;

            // PERSISTENCIA: Guardar cambio de estado
            PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        }
    }
}