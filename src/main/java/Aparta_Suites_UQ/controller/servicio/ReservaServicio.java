package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.model.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaServicio {

    private static ReservaServicio instance;
    private Reserva reservaEditar;

    private ReservaServicio() {
    }
    public static ReservaServicio getInstance() {
        if (instance == null) {
            instance = new ReservaServicio();
        }
        return instance;
    }
    public Reserva getReservaEditar() {
        return reservaEditar;
    }
    public void setReservaEditar(Reserva reserva) {
        this.reservaEditar = reserva;
    }
    public List<Reserva> listarReservas() {
        return Empresa.getInstance().getListaReservas();
    }
    public List<Reserva> refrescarEstados() {
        List<Reserva> lista = listarReservas();
        for (Reserva reserva : lista) {
            reserva.validarEstadosDirectos();
        }
        return lista;
    }

    public List<Reserva> filtrarReservas(String texto, String filtroEstado) {
        String busqueda = texto == null ? "" : texto.trim().toLowerCase();
        return listarReservas().stream()
                .filter(r -> {
                    boolean coincideTexto = busqueda.isEmpty()
                            || r.getCodigo().toLowerCase().contains(busqueda)
                            || (r.getTitular() != null && r.getTitular().getNombre().toLowerCase().contains(busqueda));
                    boolean coincideEstado = filtroEstado == null || filtroEstado.equals("Todos")
                            || r.getEstado().name().equals(filtroEstado);
                    return coincideTexto && coincideEstado;
                })
                .collect(Collectors.toList());
    }

    public void eliminarReserva(Reserva reserva) {
        listarReservas().remove(reserva);
    }
    public void validarDatos(DatosReserva d) {
        if (estaVacio(d.cantidadOcupantes()) || estaVacio(d.correo()) || estaVacio(d.documento())
                || estaVacio(d.nombre()) || estaVacio(d.telefono()) || d.fechaEntrada() == null
                || d.fechaNacimiento() == null || d.fechaSalida() == null || estaVacio(d.anticipo())) {
            throw new IllegalArgumentException("Debes rellenar todos los campos requeridos.");
        }
        Period periodo = Period.between(d.fechaNacimiento(), LocalDate.now());
        if (periodo.getYears() < 18) {
            throw new IllegalArgumentException("¡Se debe ser mayor de edad para realizar una reserva!");
        }
        if (d.fechaEntrada().isAfter(d.fechaSalida())) {
            throw new IllegalArgumentException("¡La fecha de entrada es después de la fecha de salida!");
        }
        if (d.fechaEntrada().isEqual(d.fechaSalida())) {
            throw new IllegalArgumentException("¡La fecha de entrada y salida no pueden ser iguales!");
        }
    }
    public void validarDisponibilidad(DatosReserva d) {
        if (d.fechaEntrada() == null || d.fechaSalida() == null || d.apartamento() == null) {
            throw new IllegalArgumentException("Por favor seleccione las fechas de entrada/salida y el apartamento.");
        }
        for (Reserva reserva : listarReservas()) {
            if (reserva.getEstado() != EstadoReserva.CANCELADA && !reserva.equals(reservaEditar)) {
                if (reserva.getApartamento() != null
                        && reserva.getApartamento().getId().equals(d.apartamento().getId())
                        && reserva.getEstancia().seSolapaCon(d.fechaEntrada(), d.fechaSalida())) {
                    throw new IllegalArgumentException(
                            "No se puede crear la reserva porque el apartamento ya está reservado entre el "
                                    + d.fechaEntrada() + " y el " + d.fechaSalida() + ".");
                }
            }
        }
    }
    public int parseCantidadOcupantes(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Verifique que los campos numéricos tengan valores válidos.");
        }
    }
    public void actualizarReserva(DatosReserva d) {
        Reserva reserva = reservaEditar;
        if (reserva == null) {
            return;
        }
        reserva.getEstancia().setFechaEntrada(d.fechaEntrada());
        reserva.getEstancia().setFechaSalida(d.fechaSalida());
        reserva.setApartamento(d.apartamento());
        reserva.setEstado(d.estado());
        Huesped titular = reserva.getTitular();
        if (titular != null) {
            titular.setNombre(d.nombre().trim());
            titular.setDocumento(d.documento().trim());
            titular.setTelefono(d.telefono().trim());
            titular.setCorreo(d.correo().trim());
            titular.setFechaNacimiento(d.fechaNacimiento());
        }
        reservaEditar = null;
    }
    public Reserva crearReservaDirecta(DatosReserva d, List<Ocupante> ocupantes) {
        double anticipo = 0;
        String textoAnticipo = d.anticipo() == null ? "" : d.anticipo().trim();
        try {
            if (!textoAnticipo.isEmpty()) {
                anticipo = Double.parseDouble(textoAnticipo);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Verifique que los campos numéricos tengan valores válidos.");
        }
        try {
            Estancia estancia = new Estancia(d.fechaEntrada(), d.fechaSalida());
            List<Temporada> temporadas = Empresa.getInstance().getListaTemporadas();
            Huesped huesped = new Huesped(d.nombre().trim(), d.documento().trim(), d.fechaNacimiento(),
                    d.telefono().trim(), d.correo().trim());
            double valorEstancia = estancia.calcularValorTotal(d.apartamento(), ocupantes, temporadas);
            Reserva reserva = new Reserva(estancia, CanalReserva.DIRECTA, huesped, d.apartamento(),
                    ocupantes, valorEstancia);
            reserva.getFolio().agregarCargo(
                    new Cargo("Alojamiento reserva (" + estancia.getNoches() + " noches)", valorEstancia));
            if (anticipo > 0) {
                reserva.getFolio().registrarPago(new Pago(anticipo, MedioPago.EFECTIVO));
            }
            listarReservas().add(reserva);
            return reserva;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error al crear la reserva: " + e.getMessage());
        }
    }
    public boolean puedeCheckIn(Reserva reserva) {
        return reserva != null && reserva.getEstado() == EstadoReserva.CONFIRMADA;
    }

    public boolean puedeCheckOut(Reserva reserva) {
        return reserva != null && reserva.getEstado() == EstadoReserva.EN_CURSO;
    }

    public boolean puedeDeclararNoShow(Reserva reserva) {
        return reserva != null && reserva.getEstado() == EstadoReserva.CONFIRMADA;
    }
    public boolean realizarCheckIn(Reserva reserva) {
        if (reserva.getApartamento().getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            reserva.getApartamento().setEstadoOperativo(EstadoOperativo.PREPARADO);
        }
        if (reserva.hacerCheckIn()) {
            reserva.setEstado(EstadoReserva.EN_CURSO);
            reserva.getApartamento().setEstadoOperativo(EstadoOperativo.OCUPADO);
            return true;
        }
        return false;
    }
    public void realizarCheckOut(Reserva reserva) {
        reserva.hacerCheckOut();
    }

    public void declararNoShow(Reserva reserva) {
        reserva.declararNoShow();
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
