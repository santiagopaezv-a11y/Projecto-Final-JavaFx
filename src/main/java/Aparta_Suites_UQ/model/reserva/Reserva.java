package Aparta_Suites_UQ.model.reserva;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.transacciones.Temporada;
import Aparta_Suites_UQ.utils.Navegacion;

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


    private String codigo;
    private String codigoExterno; // Opcional, para el canal Externo
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private LocalDateTime fechaCreacion;

    private CanalReserva canal;
    private EstadoReserva estado;

    private Huesped titular;
    private Apartamento apartamento;
    private List<Ocupante> ocupantes;
    private Folio folio;
    // Valores congelados al momento de crear la reserva
    private double valorAlojamientoCongelado;
    private String politicaCancelacionAplicada;

    public Reserva(String codigo, LocalDate fechaEntrada, LocalDate fechaSalida,
                   CanalReserva canal, Huesped titular, Apartamento apartamento) {
        this(codigo, null, fechaEntrada,fechaSalida,canal, titular, apartamento);
    }
    public Reserva(String codigo, String codigoExterno, LocalDate fechaEntrada, LocalDate fechaSalida,
                   CanalReserva canal, Huesped titular, Apartamento apartamento) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la reserva no puede estar vacio. ");
        }
        if (canal == null || titular == null || apartamento == null) {
            throw new IllegalArgumentException("El canal, titular y apartamento no pueden ser nulos. ");
        }
        validarFechas(fechaEntrada, fechaSalida);

        this.codigo = codigo.trim();
        this.codigoExterno = (codigoExterno != null) ? codigoExterno.trim() : null;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaCreacion = LocalDateTime.now();
        this.canal = canal;
        this.estado = EstadoReserva.PENDIENTE; // Toda reserva nace Pendiente
        this.titular = titular;
        this.apartamento = apartamento;
        this.ocupantes = new ArrayList<>();
        this.folio = new Folio();
    }
    // Validaciones

    private void validarFechas(LocalDate entrada, LocalDate salida) {
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy.");
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada (mínimo 1 noche).");
        }
    }
    public boolean seSolapaCon (LocalDate inicio, LocalDate fin) {
        if (this.estado == EstadoReserva.CANCELADA || this.estado == EstadoReserva.NO_SHOW) {
            return false;
        }
        if (inicio == null || fin == null) {
            return false;
        }
        return inicio.isBefore(this.fechaSalida) && fin.isAfter(this.fechaEntrada);
    }

    public long getNoches() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }
    // CALCULOS

    public int calcularComision(List<Temporada> temporadas){
        Estancia estancia = new Estancia(this.fechaEntrada, this.fechaSalida);
        int valorEstancia = estancia.calcularValorTotal(this.apartamento, this.ocupantes, temporadas);
        return  this.canal.calcularComision(valorEstancia);
    }

    public int calcularAnticipoRequerido(List<Temporada> temporadas){
        Estancia estancia = new Estancia(this.fechaEntrada, this.fechaSalida);
        int valorEstancia = estancia.calcularValorTotal(this.apartamento, this.ocupantes, temporadas);
        return this.canal.calcularAnticipoRequerido(valorEstancia);
    }

    public int calcularValorTotalEstancia(List<Temporada> temporadas){
        Estancia estancia = new Estancia(this.fechaEntrada, this.fechaSalida);
        return estancia.calcularValorTotal(this.apartamento, this.ocupantes, temporadas);
    }

    // OCUPANTES

    public void agregarOcupante(Ocupante ocupante) {
        if (ocupante == null) {
            throw new IllegalArgumentException("El ocupante no puede ser nulo. ");
        }
        int totalPersonasFuturo = 1 + this.ocupantes.size() + 1;
        if (totalPersonasFuturo > apartamento.getCantMaximaPersonas()) {
            throw new IllegalStateException("Se supera la capacidad máxima del apartamento ("
                    + apartamento.getCantMaximaPersonas() + " personas incluyendo al titular).");
        }
        this.ocupantes.add(ocupante);
    }

  //Para revisar que si han pasado 24 horas se cancele
    public boolean verificarVencimiento24Horas() {
        if (this.estado == EstadoReserva.PENDIENTE &&
                LocalDateTime.now().isAfter(fechaCreacion.plusHours(24))) {
            this.estado = EstadoReserva.CANCELADA;
            return true;
        }
        return false;
    }

    // Transiciones de reservas
    public void confirmar() {
        if (this.estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo una reserva Pendiente puede pasar a Confirmada.");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void hacerCheckIn() {
        if (this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede hacer check-in a una reserva confirmada");
        }else if (!LocalDate.now().isEqual(fechaEntrada)){
            throw new IllegalStateException("Solo se permite el check-in en la fecha de entrada.");
        }else if (apartamento.getEstadoOperativo() != EstadoOperativo.PREPARADO){
            throw new IllegalStateException("El apartamento no está preparado");
        }
        this.estado = EstadoReserva.EN_CURSO;
        apartamento.setEstadoOperativo(EstadoOperativo.OCUPADO);
    }

    public void hacerCheckOut() {
        if (this.estado != EstadoReserva.EN_CURSO) {
            throw new IllegalStateException("Solo se puede hacer check-out a una reserva en curso");
        }
        if (folio.getSaldo() != 0){
            throw new IllegalStateException("No se puede hacer check-out. EL folio tiene un saldo pendiente de $" + folio.getSaldo() );
        }
        this.estado = EstadoReserva.FINALIZADA;
        apartamento.setEstadoOperativo(EstadoOperativo.EN_PREPARACION);
    }

    public void cancelar() {
        if (this.estado != EstadoReserva.PENDIENTE && this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("No se puede cancelar una reserva que no esté Pendiente o Confirmada.");
        }
        this.estado = EstadoReserva.CANCELADA;
    }

    public void declararNoShow() {
        if (this.estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede declarar no-show a reservas Confirmadas.");
        }
        this.estado = EstadoReserva.NO_SHOW;
    }

    public LocalDateTime getFechaHoraEntrada(){
        return this.fechaEntrada.atTime(HORA_CHECK_IN);
    }

    public LocalDateTime getFechaHoraSalida(){
        return this.fechaSalida.atTime(HORA_CHECK_OUT);
    }

    public boolean cumpleTiempoPreparacion(Reserva reserva){
        if (!this.apartamento.equals(reserva.getApartamento())) {
            return  true;
        }
        if (this.fechaSalida.isEqual(reserva.getFechaEntrada())) {
            long horasDiferencia = ChronoUnit.HOURS.between(this.getFechaHoraSalida(), reserva.getFechaHoraEntrada());
            return horasDiferencia >= HORAS_MINIMAS_PREPARACION;
        }
        return true;
    }

 // POLITICA DE CANCELACION








    //Get codigos
    public String getCodigo() { return codigo; }
    public String getCodigoExterno() { return codigoExterno; }
    public void setCodigoExterno(String codigoExterno) { this.codigoExterno = codigoExterno; }
    //Get fechas

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    //Get enums
    public CanalReserva getCanal() { return canal; }
    public EstadoReserva getEstado() { return estado; }
    //Get huesped, apartamento, lista ocupantes, folio
    public Huesped getTitular() { return titular; }
    public Apartamento getApartamento() { return apartamento; }
    public List<Ocupante> getOcupantes() { return new ArrayList<>(ocupantes); }
    public Folio getFolio() { return folio; }
    public double getValorAlojamientoCongelado() { return valorAlojamientoCongelado; }
    public void setValorAlojamientoCongelado(double valorAlojamientoCongelado) { this.valorAlojamientoCongelado = valorAlojamientoCongelado; }
    public String getPoliticaCancelacionAplicada() { return politicaCancelacionAplicada; }
    public void setPoliticaCancelacionAplicada(String politicaCancelacionAplicada) { this.politicaCancelacionAplicada = politicaCancelacionAplicada; }
}
