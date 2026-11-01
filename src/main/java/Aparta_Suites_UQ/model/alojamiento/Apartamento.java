package Aparta_Suites_UQ.model.alojamiento;
import Aparta_Suites_UQ.model.reserva.Reserva;
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

        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.cantMaximaPersonas = cantMaximaPersonas;
        this.caracteristicas = caracteristicas;
        this.precioNoche = precioNoche;
        this.estadoOperativo = estadoOperativo;
        this.reservas = new ArrayList<>();
        this.id="Apart_"+(++numeroApartamentos);
        //this.rutaImage=rutaImage;
    }
//    public String getRutaImage() {
//        return rutaImage;
//    }

//    public void setRutaImage(String rutaImage) {
//        this.rutaImage = rutaImage;
//    }

    public boolean estaDisponible(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada ==  null || fechaSalida == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas. ");
        }
        if (this.estadoOperativo == EstadoOperativo.FUERA_DE_SERVICIO){
            return false;
        }
        for (Reserva reservaExistente : reservas) {
            if (reservaExistente.seSolapaCon(fechaEntrada, fechaSalida)){
                return false;
            }
        }
        return true;
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

    //GETTERS Y SETTERS

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
}
