package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.alojamiento.Temporada;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
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

    public ResultadoCancelacion calcularResultadoCancelacion() {
        double totalPagado = (double) this.folio.getTotalPagos();
        double valorTotalEstancia = this.valorAlojamientoCongelado;
        long diasAntelacion = ChronoUnit.DAYS.between(LocalDate.now(), this.estancia.getFechaEntrada());
        double porcentajeRetencion;
        double montoRetenido;
        double montoADevolver;

        if (this.estado == EstadoReserva.NO_SHOW || diasAntelacion < 2) {
            porcentajeRetencion = 1.0;
            montoRetenido = Math.min(totalPagado, valorTotalEstancia);
            montoADevolver = 0;
        } else if (diasAntelacion <= 7) {
            porcentajeRetencion = 0.5;
            montoRetenido = valorTotalEstancia * porcentajeRetencion;
            montoADevolver = Math.max(0, totalPagado - montoRetenido);
        } else {
            porcentajeRetencion = 0.0;
            montoRetenido = 0;
            montoADevolver = totalPagado;
        }
        return new ResultadoCancelacion(totalPagado, valorTotalEstancia, diasAntelacion, montoRetenido, montoADevolver);
    }
    // OCUPANTES
//    public void agregarOcupante(Ocupante ocupante) {
//        if (ocupante == null) {
//            throw new IllegalArgumentException("El ocupante no puede ser nulo.");
//        }
//        int totalPersonasFuturo = 1 + this.ocupantes.size() + 1;
//        if (totalPersonasFuturo > apartamento.getCantMaximaPersonas()) {
//            throw new IllegalStateException("Se supera la capacidad máxima del apartamento ("
//                    + apartamento.getCantMaximaPersonas() + " personas incluyendo al titular).");
//        }
//        this.ocupantes.add(ocupante);
//        this.cantidadOcupantes = this.ocupantes.size() + 1;
//    }
    //ValidarEstados
    public void validarEstadosDirectos(){
        if (getEstado()==EstadoReserva.PENDIENTE){
            verificarVencimiento24Horas();
        }
        if (getEstado()==EstadoReserva.PENDIENTE){
            setEstado(EstadoReserva.CONFIRMADA);
        }
        if (hacerCheckIn()){
            setEstado(EstadoReserva.EN_CURSO);
            getApartamento().setEstadoOperativo(EstadoOperativo.OCUPADO);
        }
        if (getEstado()==EstadoReserva.CONFIRMADA&&LocalDateTime.now().isAfter(getEstancia().getFechaEntrada().atTime(22,0))){
            setEstado(EstadoReserva.NO_SHOW);
        }
    }
    public void verificarVencimiento24Horas() {
        if (getEstado() == EstadoReserva.PENDIENTE &&
                LocalDateTime.now().isAfter(fechaCreacion.plusHours(24))) {
            setEstado(EstadoReserva.CANCELADA);
        }
    }

    public void confirmar() {
        if (this.estado != EstadoReserva.PENDIENTE){
            throw new IllegalStateException("Solo una reserva Pendiente puede pasar a Confirmada.");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }
    public boolean hacerCheckIn() {
        boolean verificarEstadoApartamento=true;
        boolean verificarEstadoReserva=true;
        if (getEstado() != EstadoReserva.CONFIRMADA) {
            verificarEstadoReserva=false;
        } else if (apartamento.getEstadoOperativo() != EstadoOperativo.PREPARADO){
            verificarEstadoApartamento=false;
        }
        boolean validarHora=LocalDateTime.now().isAfter(getEstancia().getFechaHoraEntrada());
        return validarHora&&verificarEstadoApartamento&&verificarEstadoReserva;
    }
    public void hacerCheckOut() {
        if (this.estado != EstadoReserva.EN_CURSO) {
            throw new IllegalStateException("Solo se puede hacer check-out a una reserva en curso.");
        }
        if (folio.getSaldo() != 0) {
            throw new IllegalStateException("No se puede hacer check-out. El folio tiene un saldo pendiente de $" + folio.getSaldo());
        }
        this.estado = EstadoReserva.FINALIZADA;
        apartamento.setEstadoOperativo(EstadoOperativo.EN_PREPARACION);
    }

    public void cancelar() {
        if (getEstado() != EstadoReserva.PENDIENTE && getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("No se puede cancelar una reserva que no esté Pendiente o Confirmada.");
        }
         setEstado(EstadoReserva.CANCELADA);
    }

    public void declararNoShow() {
        if (this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede declarar no-show a reservas Confirmadas.");
        }
        this.estado = EstadoReserva.NO_SHOW;
    }

    public boolean cumpleTiempoPreparacion(Reserva reserva) {
        if (!this.apartamento.equals(reserva.getApartamento())) {
            return true;
        }
        if (this.estancia.getFechaSalida().isEqual(reserva.getEstancia().getFechaEntrada())) {
            long horasDiferencia = ChronoUnit.HOURS.between(getEstancia().getFechaHoraSalida(), getEstancia().getFechaEntrada());
            return horasDiferencia >= HORAS_MINIMAS_PREPARACION;
        }
        return true;
    }
    // GETTERS Y SETTERS

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
}