package Aparta_Suites_UQ.model.alojamiento;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.TipoTemporada;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;
import Aparta_Suites_UQ.utils.Navegacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Apartamento {

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
    private List<Reserva> reservas;
    private List<Bloqueo> bloqueos;

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

        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.cantMaximaPersonas = cantMaximaPersonas;
        this.caracteristicas = caracteristicas;
        this.precioNoche = precioNoche;
        this.estadoOperativo = estadoOperativo;
        this.reservas = new ArrayList<>();
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


    public boolean estaDisponible(LocalDate fechaEntrada, LocalDate fechaSalida) {

        boolean tieneReservaSolapada = reservas.stream().anyMatch(reserva -> reserva.seSolapaCon(fechaEntrada, fechaSalida));

        if (tieneReservaSolapada) {
            return false;
        }

        boolean tieneBloqueoSolapado = bloqueos.stream().anyMatch(bloqueo -> bloqueo.seSolapaCon(fechaEntrada, fechaSalida));
        return !tieneBloqueoSolapado;
    }

    public void agregarReserva(Reserva nuevaReserva){
        if (nuevaReserva == null){
            throw new IllegalArgumentException("La reserva no puede ser nula. ");
        }
        if (!estaDisponible(nuevaReserva.getFechaEntrada(), nuevaReserva.getFechaSalida())){
            throw  new IllegalStateException("El apartamento " + nombre + " ya tiene una reserva en el rango seleccionado. ");
        }
        this.reservas.add(nuevaReserva);
    }

    public void ponerFueraDeServicio(String motivoFueraServicio) {
        if (motivoFueraServicio == null || motivoFueraServicio.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar un motivo para poner el apartamento fuera de servicio. ");
        }
        this.estadoOperativo = EstadoOperativo.FUERA_DE_SERVICIO;
        this.motivoFueraServicio = motivoFueraServicio;
    }

    public boolean estaHabilitadoParaReservar(){
        return this.estadoOperativo != EstadoOperativo.FUERA_DE_SERVICIO;
    }

    public boolean existeCodigoExterno(String codigoExterno){
        if (codigoExterno == null || codigoExterno.trim().isEmpty()){
            return false;
        }
        return reservas.stream().filter(reserva -> reserva.getCodigoExterno() != null).anyMatch(reserva -> reserva.getCodigoExterno().equalsIgnoreCase(codigoExterno.trim()));
    }

    //GETTERS Y SETTERS

    public int getTarifaSegunTemporada(TipoTemporada tipoTemporada){
        if (tipoTemporada == null) {
            return (int) Math.round(this.precioNoche);
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
        return new ArrayList<>(reservas);
    }

    public List<Bloqueo> getBloqueos() {
        return new ArrayList<>(bloqueos);
    }
}
