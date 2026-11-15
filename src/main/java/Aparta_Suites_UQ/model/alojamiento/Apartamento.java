package Aparta_Suites_UQ.model.alojamiento;

import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.interfaces.Reservable;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Apartamento implements Reservable, Serializable {

    private final String id;
    private String nombre;
    private int numeroDormitorios;
    private int cantMaximaPersonas;
    private String caracteristicas;
    private double precioNoche;
    private EstadoOperativo estadoOperativo;
    private String motivoFueraServicio;
    //private String rutaImage;
    static int numeroApartamentos=0;
    private boolean activo = true;
    private List<Bloqueo> bloqueos;
    private Map<TipoTemporada, Double> tarifasPorTemporada;

    public Apartamento(String nombre, int numeroDormitorios, int cantMaximaPersonas, String caracteristicas, double precioNoche, EstadoOperativo estadoOperativo ) {//String rutaImage
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del apartamento no puede estar vacio. ");
        }
        if (caracteristicas == null || caracteristicas.trim().isEmpty()) {
            throw new IllegalArgumentException("Las caracteristicas del apartamento no pueden ser nulas. ");
        }
        if (numeroDormitorios <= 0){
            throw new IllegalArgumentException("El numero de dormitorios debe ser mayor a 0. ");
        }

        if (cantMaximaPersonas <= 0){
            throw new IllegalArgumentException( "La cantidad maxima de personas debe ser mayor a 0. ");
        }

        if (estadoOperativo == null){
            throw new IllegalArgumentException("El estado operativo no puede ser nulo. ");
        }

        if (precioNoche <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor a 0. ");
        }
        this.tarifasPorTemporada = new HashMap<>();
        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.cantMaximaPersonas = cantMaximaPersonas;
        this.caracteristicas = caracteristicas;
        this.precioNoche = precioNoche;
        this.estadoOperativo = estadoOperativo;
        this.bloqueos = new ArrayList<>();
        this.id="Apart_"+(++numeroApartamentos);
        //this.rutaImage=rutaImage;
    }
//    public String getRutaImage() {
//        return rutaImage;
//    }
//    public void setRutaImage(String rutaImage) {
//        this.rutaImage = rutaImage;
//    }
    public void agregarBloqueo(Bloqueo nuevoBloqueo){
        if (nuevoBloqueo == null){
            throw new IllegalArgumentException("El bloqueo no puede estar vacio. ");
        }
        if (!estaDisponible(nuevoBloqueo.getFechaInicio(), nuevoBloqueo.getFechaFin())){
            throw new IllegalStateException("No se puede registrar el bloqueo porque el apartamento " + nombre + " tiene reservas o bloqueos solapados. ");
        }
        this.bloqueos.add(nuevoBloqueo);
    }


    @Override
    public boolean estaDisponible(LocalDate fechaEntrada, LocalDate fechaSalida) {
        return estaDisponible(fechaEntrada, fechaSalida, null);
    }

    /**
     * Sin doble venta: el apartamento no puede tener dos reservas ACTIVAS (Pendiente, Confirmada o En curso)
     * que compartan al menos una noche, ni una noche bloqueada. La noche de la fecha de salida no cuenta.
     *
     * @param reservaExcluida reserva que se ignora (la que se está editando), o null
     */
    public boolean estaDisponible(LocalDate fechaEntrada, LocalDate fechaSalida, Reserva reservaExcluida) {
        return !tieneReservaActivaEn(fechaEntrada, fechaSalida, reservaExcluida)
                && !tieneBloqueoEn(fechaEntrada, fechaSalida);
    }

    public boolean tieneReservaActivaEn(LocalDate fechaEntrada, LocalDate fechaSalida, Reserva reservaExcluida) {
        return Empresa.getInstance().getListaReservas().stream()
                .filter(r -> r != reservaExcluida)
                .filter(r -> r.getApartamento() == this)
                .filter(r -> r.getEstado().esActiva())
                .anyMatch(r -> r.getEstancia().seSolapaCon(fechaEntrada, fechaSalida));
    }

    public boolean tieneBloqueoEn(LocalDate fechaEntrada, LocalDate fechaSalida) {
        return bloqueos.stream().anyMatch(bloqueo -> bloqueo.seSolapaCon(fechaEntrada, fechaSalida));
    }

    public void eliminarBloqueo(Bloqueo bloqueo) {
        this.bloqueos.remove(bloqueo);
    }

    /** Registra la reserva (en la lista de la empresa) solo si el apartamento está libre en ese rango. */
    public void agregarReserva(Reserva nuevaReserva){
        if (nuevaReserva == null){
            throw new IllegalArgumentException("La reserva no puede ser nula. ");
        }
        if (!estaDisponible(nuevaReserva.getEstancia().getFechaEntrada(), nuevaReserva.getEstancia().getFechaSalida(), nuevaReserva)){
            throw  new IllegalStateException("El apartamento " + nombre + " ya tiene una reserva en el rango seleccionado. ");
        }
        if (!Empresa.getInstance().getListaReservas().contains(nuevaReserva)) {
            Empresa.getInstance().getListaReservas().add(nuevaReserva);
        }
    }

    public void ponerFueraDeServicio(String motivoFueraServicio) {
        if (motivoFueraServicio == null || motivoFueraServicio.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un motivo para poner el apartamento fuera de servicio. ");
        }
        this.estadoOperativo = EstadoOperativo.FUERA_DE_SERVICIO;
        this.motivoFueraServicio = motivoFueraServicio;
    }

    @Override
    public boolean estaHabilitadoParaReservar(){
        return this.activo && this.estadoOperativo != EstadoOperativo.FUERA_DE_SERVICIO;
    }

    public boolean existeCodigoExterno(String codigoExterno){
        if (codigoExterno == null || codigoExterno.trim().isEmpty()){
            return false;
        }
        return getReservas().stream().filter(reserva -> reserva.getCodigoExterno() != null).anyMatch(reserva -> reserva.getCodigoExterno().equalsIgnoreCase(codigoExterno.trim()));
    }

    //GETTERS Y SETTERS

    public int getTarifaSegunTemporada(TipoTemporada tipoTemporada){
        if (tipoTemporada == null) {
            return (int) Math.round(this.precioNoche);
        }
        // Tarifa propia de este apartamento para la temporada, definida por el administrador
        Double tarifaPropia = tarifasPorTemporada.get(tipoTemporada);
        if (tarifaPropia != null && tarifaPropia > 0) {
            return (int) Math.round(tarifaPropia);
        }

        switch (tipoTemporada){
            case ALTA:
                return (int) Math.round(this.precioNoche * 1.50);

            case MEDIA:
                return (int) Math.round(this.precioNoche * 1.20);

            case BAJA:
            default:
                return (int) Math.round(this.precioNoche);
        }
    }

    public String getId() {
        return id;
    }

    public static int getNumeroApartamentos() {
        return numeroApartamentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("EL nombre del apartamento no puede estar vacio. ");
        }
        this.nombre = nombre.trim();
    }

    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }

    public void setNumeroDormitorios(int numeroDormitorios) {
        if (numeroDormitorios <= 0) {
            throw new IllegalArgumentException("El numero de dormitorios debe ser mayor a 0. ");
        }
        this.numeroDormitorios = numeroDormitorios;
    }

    public int getCantMaximaPersonas() {
        return cantMaximaPersonas;
    }

    public void setCantMaximaPersonas(int cantMaximaPersonas) {
        if (cantMaximaPersonas <= 0) {
            throw new IllegalArgumentException("La cantidad maxima de personas debe ser mayor a 0. ");
        }
        this.cantMaximaPersonas = cantMaximaPersonas;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        if (caracteristicas == null || caracteristicas.trim().isEmpty()) {
            throw new IllegalArgumentException("Las caracteristicas no pueden estar vacias");
        }
        this.caracteristicas = caracteristicas;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        if (precioNoche <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a 0. ");
        }
        this.precioNoche = precioNoche;
    }

    public EstadoOperativo getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(EstadoOperativo estadoOperativo) {
        if (estadoOperativo == null) {
            throw new IllegalArgumentException("El estado operativo no puede ser nulo. ");
        }
        this.estadoOperativo = estadoOperativo;
        if (estadoOperativo != EstadoOperativo.FUERA_DE_SERVICIO) {
            this.motivoFueraServicio = null;
        }
    }

    public String getMotivoFueraServicio() {
        return motivoFueraServicio;
    }

    public void setMotivoFueraServicio(String motivoFueraServicio) {
        this.motivoFueraServicio = motivoFueraServicio;
    }

    public List<Reserva> getReservas() {
        return Empresa.getInstance().getListaReservas().stream()
                .filter(r -> r.getApartamento() == this)
                .collect(java.util.stream.Collectors.toList());
    }

    /** Eliminación lógica: el apartamento deja de mostrarse y de reservarse, pero su historial se conserva. */
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Bloqueo> getBloqueos() {
        return new ArrayList<>(bloqueos);
    }
    public void setTarifa(TipoTemporada tipo, double precio) {
        tarifasPorTemporada.put(tipo, precio);
    }

    /** Tarifa efectiva por persona por noche en esa temporada (propia o la derivada de la tarifa base). */
    public double getTarifa(TipoTemporada tipo) {
        return getTarifaSegunTemporada(tipo);
    }
}
