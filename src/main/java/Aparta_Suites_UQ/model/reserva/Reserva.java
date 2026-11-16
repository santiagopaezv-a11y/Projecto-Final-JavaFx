package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.interfaces.Cancelable;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reserva implements Cancelable, Serializable {
    // CONSTANTES
    public static final java.time.LocalTime HORA_CHECK_OUT = java.time.LocalTime.of(11, 0);
    public static final java.time.LocalTime HORA_CHECK_IN = java.time.LocalTime.of(15, 0);
    public static final long HORAS_MINIMAS_PREPARACION = 3;

    private final String codigo;
    private String codigoExterno;
    private Estancia estancia;
    private LocalDateTime fechaCreacion;
    private CanalReserva canal;
    private EstadoReserva estado;
    private Huesped titular;
    private Apartamento apartamento;
    private List<Ocupante> ocupantes;
    private Folio folio;
    private static int cantidadReserva;
    private int cantidadOcupantes = 0;
    // Valores congelados al momento de crear la reserva
    private double valorAlojamientoCongelado;
    private String politicaCancelacionAplicada;
    private PoliticaCancelacion politica;
    private ResultadoCancelacion resultadoCancelacion;

    public Reserva(Estancia estancia, CanalReserva canal, Huesped titular, Apartamento apartamento, List<Ocupante> ocupantes, double valorAlojamientoCongelado) {
        if (estancia == null) {
            throw new IllegalArgumentException("La estancia no puede ser nula.");
        }
        this.estancia = estancia;
        this.fechaCreacion = LocalDateTime.now();
        this.canal = canal;
        this.estado = EstadoReserva.PENDIENTE;
        this.titular = titular;
        this.codigo = "" + (++cantidadReserva);
        this.apartamento = apartamento;
        this.ocupantes = ocupantes != null ? ocupantes : new ArrayList<>();
        this.folio = new Folio();
        this.cantidadOcupantes = this.ocupantes.size() + 1;
        this.valorAlojamientoCongelado = valorAlojamientoCongelado;
        this.politica = Empresa.getInstance().getPoliticaCancelacion();
        this.politicaCancelacionAplicada = this.politica.descripcion();
    }

    public Reserva(String codigoExterno, Estancia estancia, CanalReserva canal, Huesped titular, Apartamento apartamento, List<Ocupante> ocupantes,double valorAlojamientoCongelado) {
        this(estancia, canal, titular, apartamento, ocupantes,valorAlojamientoCongelado);
        this.codigoExterno = codigoExterno;
    }
    public int calcularComision() {
        return this.canal.calcularComision((int) this.valorAlojamientoCongelado);
    }

    public int calcularAnticipoRequerido() {
        return this.canal.calcularAnticipoRequerido((int) this.valorAlojamientoCongelado);
    }

    @Override
    public ResultadoCancelacion calcularResultadoCancelacion() {
        double totalPagado = this.folio.getTotalPagos();
        double valorTotalEstancia = this.valorAlojamientoCongelado;
        long diasAntelacion = ChronoUnit.DAYS.between(LocalDate.now(), this.estancia.getFechaEntrada());
        double montoRetenido;
        double montoADevolver;
        if (this.estado == EstadoReserva.NO_SHOW || diasAntelacion < politica.getDiasRetencionParcial()) {
            montoRetenido = Math.min(totalPagado, valorTotalEstancia);
            montoADevolver = Math.max(0, totalPagado - montoRetenido);
        } else if (diasAntelacion <= politica.getDiasDevolucionTotal()) {
            montoRetenido = valorTotalEstancia * politica.getPorcentajeRetencionParcial();
            montoADevolver = Math.max(0, totalPagado - montoRetenido);
        } else {
            montoRetenido = 0;
            montoADevolver = totalPagado;
        }
        return new ResultadoCancelacion(totalPagado, valorTotalEstancia, diasAntelacion, montoRetenido, montoADevolver);
    }

    /**
     * Cambios de estado que ocurren solos por el paso del tiempo (nunca confirma ni hace check-in):
     * una Pendiente sin confirmar en 24 horas pasa a Cancelada, y una Confirmada cuyo titular no llegó
     * antes de las 10:00 p. m. del día de entrada pasa a No-show.
     */
    public void actualizarEstadosAutomaticos() {
        if (getEstado() == EstadoReserva.PENDIENTE) {
            verificarVencimiento24Horas();
        }
        if (getEstado() == EstadoReserva.CONFIRMADA
                && LocalDateTime.now().isAfter(getEstancia().getFechaEntrada().atTime(22, 0))) {
            declararNoShow();
        }
    }

    public void verificarVencimiento24Horas() {
        if (getEstado() == EstadoReserva.PENDIENTE &&
                LocalDateTime.now().isAfter(fechaCreacion.plusHours(24))) {
            setEstado(EstadoReserva.CANCELADA);
        }
    }

    /** Pendiente -> Confirmada, siempre que se haya pagado el anticipo si el canal lo exige. */
    public void confirmar() {
        if (this.estado != EstadoReserva.PENDIENTE){
            throw new IllegalStateException("Solo una reserva Pendiente puede pasar a Confirmada.");
        }
        int anticipo = calcularAnticipoRequerido();
        if (folio.getTotalPagos() < anticipo) {
            throw new IllegalStateException("Para confirmar la reserva se requiere un anticipo de $" + anticipo
                    + " (pagado: $" + (long) folio.getTotalPagos() + ").");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }
    /**
     * Check-in: solo si la reserva está Confirmada, ya llegó la hora de entrada (3:00 p. m. del día de entrada)
     * y el apartamento está Preparado.
     *
     * @return null si se puede hacer, o el motivo por el que se rechaza
     */
    public String motivoRechazoCheckIn() {
        if (getEstado() != EstadoReserva.CONFIRMADA) {
            return "Solo se puede hacer check-in a una reserva Confirmada (estado actual: " + getEstado() + ").";
        }
        if (!LocalDateTime.now().isAfter(getEstancia().getFechaHoraEntrada())) {
            return "Aún no llega la hora de entrada: el check-in se habilita el "
                    + getEstancia().getFechaEntrada() + " desde las " + HORA_CHECK_IN + ".";
        }
        if (apartamento.getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            return "El apartamento debe estar en estado Preparado (estado actual: "
                    + apartamento.getEstadoOperativo() + ").";
        }
        return null;
    }

    public boolean hacerCheckIn() {
        return motivoRechazoCheckIn() == null;
    }

    /** Confirmada -> En curso; el apartamento pasa a Ocupado. Lanza IllegalStateException con el motivo si no procede. */
    public void registrarLlegada() {
        String motivo = motivoRechazoCheckIn();
        if (motivo != null) {
            throw new IllegalStateException(motivo);
        }
        this.estado = EstadoReserva.EN_CURSO;
        apartamento.setEstadoOperativo(EstadoOperativo.OCUPADO);
    }

    /**
     * Cambia de estado respetando el ciclo de vida; cualquier otro cambio se rechaza.
     * Pendiente -> Confirmada | Cancelada; Confirmada -> En curso | Cancelada | No-show; En curso -> Finalizada.
     */
    public void cambiarEstado(EstadoReserva nuevo) {
        if (nuevo == null || nuevo == this.estado) {
            return;
        }
        switch (this.estado) {
            case PENDIENTE:
                if (nuevo == EstadoReserva.CONFIRMADA) { confirmar(); return; }
                if (nuevo == EstadoReserva.CANCELADA) { cancelar(); return; }
                break;
            case CONFIRMADA:
                if (nuevo == EstadoReserva.EN_CURSO) { registrarLlegada(); return; }
                if (nuevo == EstadoReserva.CANCELADA) { cancelar(); return; }
                if (nuevo == EstadoReserva.NO_SHOW) { declararNoShow(); return; }
                break;
            case EN_CURSO:
                if (nuevo == EstadoReserva.FINALIZADA) { hacerCheckOut(); return; }
                break;
            default:
                break;
        }
        throw new IllegalStateException("Cambio de estado no permitido: " + this.estado + " -> " + nuevo + ".");
    }

    public void hacerCheckOut() {
        if (this.estado != EstadoReserva.EN_CURSO) {
            throw new IllegalStateException("Solo se puede hacer check-out a una reserva en curso.");
        }
        if (folio.getSaldo() != 0) {
            throw new IllegalStateException("No se puede hacer check-out. El folio tiene un saldo pendiente de $" + folio.getSaldo());
        }
        this.estado = EstadoReserva.FINALIZADA;
        apartamento.setEstadoOperativo(EstadoOperativo.PENDIENTE);
    }

    @Override
    public boolean puedeCancelarse() {
        return getEstado() == EstadoReserva.PENDIENTE || getEstado() == EstadoReserva.CONFIRMADA;
    }

    @Override
    public void cancelar() {
        if (!puedeCancelarse()) {
            throw new IllegalStateException("No se puede cancelar una reserva que no esté Pendiente o Confirmada.");
        }
        this.resultadoCancelacion = calcularResultadoCancelacion();
        setEstado(EstadoReserva.CANCELADA);
    }

    public void declararNoShow() {
        if (this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede declarar no-show a reservas Confirmadas.");
        }
        this.estado = EstadoReserva.NO_SHOW;
        this.resultadoCancelacion = calcularResultadoCancelacion();
    }

    /**
     * Entre una salida (11:00 a. m.) y la siguiente entrada (3:00 p. m.) del mismo apartamento
     * deben pasar al menos {@link #HORAS_MINIMAS_PREPARACION} horas.
     */
    public boolean cumpleTiempoPreparacion(Reserva otra) {
        if (otra == null || this.apartamento == null || !this.apartamento.equals(otra.getApartamento())) {
            return true;
        }
        if (this.estancia.getFechaSalida().isEqual(otra.getEstancia().getFechaEntrada())) {
            return ChronoUnit.HOURS.between(this.estancia.getFechaHoraSalida(), otra.getEstancia().getFechaHoraEntrada())
                    >= HORAS_MINIMAS_PREPARACION;
        }
        if (otra.getEstancia().getFechaSalida().isEqual(this.estancia.getFechaEntrada())) {
            return ChronoUnit.HOURS.between(otra.getEstancia().getFechaHoraSalida(), this.estancia.getFechaHoraEntrada())
                    >= HORAS_MINIMAS_PREPARACION;
        }
        return true;
    }

    /** Resultado de la devolución registrado al cancelar o declarar no-show (null si no ha ocurrido). */
    public ResultadoCancelacion getResultadoCancelacion() { return resultadoCancelacion; }

    public PoliticaCancelacion getPolitica() { return politica; }

    public Estancia getEstancia() { return estancia; }
    public void setEstancia(Estancia estancia) { this.estancia = estancia; }

    public String getCodigo() { return codigo; }
    public String getCodigoExterno() { return codigoExterno; }
    public void setCodigoExterno(String codigoExterno) { this.codigoExterno = codigoExterno; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public CanalReserva getCanal() { return canal; }
    public void setCanal(CanalReserva canal) { this.canal = canal; }

    public Huesped getTitular() { return titular; }
    public void setTitular(Huesped titular) { this.titular = titular; }

    public Apartamento getApartamento() { return apartamento; }
    public void setApartamento(Apartamento apartamento) { this.apartamento = apartamento; }

    public List<Ocupante> getOcupantes() { return new ArrayList<>(ocupantes); }
    public void setOcupantes(List<Ocupante> ocupantes) {
        this.ocupantes = ocupantes;
        this.cantidadOcupantes = (ocupantes != null ? ocupantes.size() : 0) + 1;
    }

    public Folio getFolio() { return folio; }
    public void setFolio(Folio folio) { this.folio = folio; }

    public double getValorAlojamientoCongelado() { return valorAlojamientoCongelado; }
    public void setValorAlojamientoCongelado(double valorAlojamientoCongelado) { this.valorAlojamientoCongelado = valorAlojamientoCongelado; }

    public String getPoliticaCancelacionAplicada() { return politicaCancelacionAplicada; }
    public void setPoliticaCancelacionAplicada(String politicaCancelacionAplicada) { this.politicaCancelacionAplicada = politicaCancelacionAplicada; }

    public int getCantidadOcupantes() { return cantidadOcupantes; }
    public void setCantidadOcupantes(int cantidadOcupantes) { this.cantidadOcupantes = cantidadOcupantes; }

    public static int getCantidadReserva() {
        return cantidadReserva;
    }

    public static void setCantidadReserva(int cantidadReserva) {
        Reserva.cantidadReserva = cantidadReserva;
    }
}