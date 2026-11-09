package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.model.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.reserva.Reserva;

import java.util.ArrayList;
import java.util.List;

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
        return Empresa.getInstance().getListaApartamentos();
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
        return apartamento != null && listarApartamentos().remove(apartamento);
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
                                   String estadoTxt, String motivoTxt) {
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
        boolean fueraDeServicio = estado == EstadoOperativo.FUERA_DE_SERVICIO;
        String motivo = motivoTxt == null ? "" : motivoTxt.trim();
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        String caracteristicasLimpias = caracteristicas == null ? "" : caracteristicas.trim();

        if (apartamentoSeleccionado != null) {
            // Modo edición
            if (fueraDeServicio && motivo.isEmpty()) {
                throw new IllegalArgumentException("Escribe el motivo del fuera de servicio");
            }
            apartamentoSeleccionado.setNumeroDormitorios(dormitorios);
            apartamentoSeleccionado.setCantMaximaPersonas(capacidad);
            apartamentoSeleccionado.setCaracteristicas(caracteristicasLimpias);
            apartamentoSeleccionado.setPrecioNoche(precioNoche);
            apartamentoSeleccionado.setEstadoOperativo(estado);
            apartamentoSeleccionado.setMotivoFueraServicio(fueraDeServicio ? motivo : null);
            apartamentoSeleccionado = null;
        } else {
            // Modo creación
            Apartamento nuevo = new Apartamento(nombreLimpio, dormitorios, capacidad,
                    caracteristicasLimpias, precioNoche, estado);
            nuevo.setMotivoFueraServicio(fueraDeServicio ? motivo : null);
            listarApartamentos().add(nuevo);
        }
    }
    public void actualizarEstadoOperativo(EstadoOperativo estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Por favor seleccione un estado operativo.");
        }
        if (apartamentoSeleccionado != null) {
            apartamentoSeleccionado.setEstadoOperativo(estado);
            apartamentoSeleccionado = null;
        }
    }
}
