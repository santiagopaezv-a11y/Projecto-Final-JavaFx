package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.reserva.ResultadoCancelacion;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;
import Aparta_Suites_UQ.utils.PersistenciaUtil;

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

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public Reserva getReservaEditar() {
        return reservaEditar;
    }

    public void setReservaEditar(Reserva reserva) {
        this.reservaEditar = reserva;
    }

    // ==========================================
    // CONSULTAS Y REGLAS DE TIEMPO AUTOMÁTICAS
    // ==========================================

    public List<Reserva> listarReservas() {
        return Empresa.getInstance().getListaReservas();
    }

    /**
     * Refresca el estado de todas las reservas ejecutando las reglas automáticas de tiempo
     * (Vencimiento de 24h sin abono y No-Show a las 10:00 PM) y persiste los cambios.
     */
    public List<Reserva> refrescarEstados() {
        List<Reserva> lista = listarReservas();
        for (Reserva reserva : lista) {
            reserva.actualizarEstadosAutomaticos();
        }
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        return lista;
    }

    public List<Reserva> filtrarReservas(String texto, String filtroEstado) {
        String busqueda = texto == null ? "" : texto.trim().toLowerCase();
        return refrescarEstados().stream()
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
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
    }

    // ==========================================
    // VALIDACIONES Y CREACIÓN / EDICIÓN
    // ==========================================

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
        if (reservaEditar == null && d.fechaEntrada().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden crear reservas con fecha de entrada anterior a hoy.");
        }
    }

    public void validarDisponibilidad(DatosReserva d) {
        if (d.fechaEntrada() == null || d.fechaSalida() == null || d.apartamento() == null) {
            throw new IllegalArgumentException("Por favor seleccione las fechas de entrada/salida y el apartamento.");
        }
        Apartamento apartamento = d.apartamento();
        if (!apartamento.estaHabilitadoParaReservar()) {
            throw new IllegalArgumentException("El apartamento " + apartamento.getNombre()
                    + " no está habilitado para reservar (fuera de servicio o eliminado).");
        }
        int totalPersonas = parseCantidadOcupantes(d.cantidadOcupantes()) + 1;
        if (totalPersonas > apartamento.getCantMaximaPersonas()) {
            throw new IllegalArgumentException("Capacidad excedida: " + apartamento.getNombre() + " admite máximo "
                    + apartamento.getCantMaximaPersonas() + " personas y la reserva tiene " + totalPersonas
                    + " (titular + ocupantes, incluidos los menores que no pagan).");
        }
        if (apartamento.tieneReservaActivaEn(d.fechaEntrada(), d.fechaSalida(), reservaEditar)) {
            throw new IllegalArgumentException(
                    "No se puede crear la reserva porque el apartamento ya está reservado entre el "
                            + d.fechaEntrada() + " y el " + d.fechaSalida() + ".");
        }
        if (apartamento.tieneBloqueoEn(d.fechaEntrada(), d.fechaSalida())) {
            throw new IllegalArgumentException("El apartamento " + apartamento.getNombre()
                    + " tiene un bloqueo registrado en esas fechas y no está disponible.");
        }
    }

    public void actualizarReserva(DatosReserva d) {
        Reserva reserva = reservaEditar;
        if (reserva == null) {
            return;
        }
        validarDisponibilidad(d);
        try {
            reserva.getEstancia().setFechaEntrada(d.fechaEntrada());
            reserva.getEstancia().setFechaSalida(d.fechaSalida());
            reserva.setApartamento(d.apartamento());
            Huesped titular = reserva.getTitular();
            if (titular != null) {
                titular.setNombre(d.nombre().trim());
                titular.setDocumento(d.documento().trim());
                titular.setTelefono(d.telefono().trim());
                titular.setCorreo(d.correo().trim());
                titular.setFechaNacimiento(d.fechaNacimiento());
            }
            reserva.cambiarEstado(d.estado());
            PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        reservaEditar = null;
    }

    public Reserva crearReservaDirecta(DatosReserva d, List<Ocupante> ocupantes) {
        return crearReserva(CanalReserva.DIRECTA, null, d, ocupantes);
    }

    public Reserva crearReserva(CanalReserva canal, String codigoExterno, DatosReserva d, List<Ocupante> ocupantes) {
        double anticipo = 0;
        String textoAnticipo = d.anticipo() == null ? "" : d.anticipo().trim();
        try {
            if (!textoAnticipo.isEmpty()) {
                anticipo = Double.parseDouble(textoAnticipo);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Verifique que los campos numéricos tengan valores válidos.");
        }
        if (anticipo < 0) {
            throw new IllegalArgumentException("El anticipo no puede ser negativo.");
        }
        List<Ocupante> lista = ocupantes == null ? List.of() : ocupantes;
        if (lista.size() != parseCantidadOcupantes(d.cantidadOcupantes())) {
            throw new IllegalArgumentException("Faltan datos de ocupantes: se esperaban "
                    + parseCantidadOcupantes(d.cantidadOcupantes()) + " y se registraron " + lista.size() + ".");
        }
        validarDisponibilidad(d);
        try {
            Estancia estancia = new Estancia(d.fechaEntrada(), d.fechaSalida());
            Huesped huesped = obtenerOCrearHuesped(d);
            double valorEstancia = estancia.calcularValorTotal(d.apartamento(), lista,
                    Empresa.getInstance().getListaTemporadas());
            Reserva reserva = codigoExterno == null
                    ? new Reserva(estancia, canal, huesped, d.apartamento(), lista, valorEstancia)
                    : new Reserva(codigoExterno, estancia, canal, huesped, d.apartamento(), lista, valorEstancia);
            reserva.getFolio().agregarCargo(
                    new Cargo("Alojamiento reserva (" + estancia.getNoches() + " noches)", valorEstancia));
            if (anticipo > 0) {
                reserva.getFolio().registrarPago(new Pago(anticipo, MedioPago.EFECTIVO));
            }
            listarReservas().add(reserva);
            PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
            return reserva;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error al crear la reserva: " + e.getMessage());
        }
    }

    private Huesped obtenerOCrearHuesped(DatosReserva d) {
        Huesped existente = Empresa.getInstance().buscarHuesped(d.documento());
        if (existente != null) {
            existente.setTelefono(d.telefono().trim());
            existente.setCorreo(d.correo().trim());
            return existente;
        }
        Huesped nuevo = new Huesped(d.nombre().trim(), d.documento().trim(), d.fechaNacimiento(),
                d.telefono().trim(), d.correo().trim());
        Empresa.getInstance().getListaHuespedes().add(nuevo);
        return nuevo;
    }

    // ==========================================
    // OPERACIONES DE CICLO DE VIDA (CHECK-IN / OUT / CANCELAR)
    // ==========================================

    public boolean puedeConfirmar(Reserva reserva) {
        return reserva != null && reserva.getEstado() == EstadoReserva.PENDIENTE;
    }

    public void confirmarReserva(Reserva reserva) {
        try {
            reserva.confirmar();
            PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public ResultadoCancelacion cancelarReserva(Reserva reserva) {
        try {
            reserva.cancelar();
            PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return reserva.getResultadoCancelacion();
    }

    public String describirResultado(ResultadoCancelacion r) {
        return String.format(java.util.Locale.of("es", "CO"),
                "Días de antelación: %d%nTotal pagado: $%,.0f%nSe retiene: $%,.0f%nSe devuelve: $%,.0f%n(El sistema registra la devolución; no mueve dinero real.)",
                r.getDiasAntelacion(), r.getTotalPagado(), r.getMontoRetenido(), r.getMontoADevolver());
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

    public void realizarCheckIn(Reserva reserva) {
        reserva.registrarLlegada();
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
    }

    public void realizarCheckOut(Reserva reserva) {
        reserva.hacerCheckOut();
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
    }

    public void declararNoShow(Reserva reserva) {
        reserva.declararNoShow();
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    public int parseCantidadOcupantes(String texto) {
        try {
            int cantidad = Integer.parseInt(texto.trim());
            if (cantidad < 0) {
                throw new NumberFormatException();
            }
            return cantidad;
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Verifique que los campos numéricos tengan valores válidos.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}