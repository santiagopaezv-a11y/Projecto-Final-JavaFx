package Aparta_Suites_UQ.model.alojamiento;

public class Apartamento {
    private final String id;
    private String nombre;
    private int numeroDormitorios;
    private int cantMaximaPersonas;
    private String caracteristicas;
    private double precioNoche;
    private EstadoOperativo estadoOperativo;
    //private String rutaImage;
    static int numeroApartamentos=0;

    public Apartamento(String nombre, int numeroDormitorios, int cantMaximaPersonas, String caracteristicas, double precioNoche, EstadoOperativo estadoOperativo ) {//String rutaImage
        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.cantMaximaPersonas = cantMaximaPersonas;
        this.caracteristicas = caracteristicas;
        this.precioNoche = precioNoche;
        this.estadoOperativo = estadoOperativo;
        this.id="Apart_"+(++numeroApartamentos);
        //this.rutaImage=rutaImage;
    }

//    public String getRutaImage() {
//        return rutaImage;
//    }

//    public void setRutaImage(String rutaImage) {
//        this.rutaImage = rutaImage;
//    }

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
        this.nombre = nombre;
    }

    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }

    public void setNumeroDormitorios(int numeroDormitorios) {
        this.numeroDormitorios = numeroDormitorios;
    }

    public int getCantMaximaPersonas() {
        return cantMaximaPersonas;
    }

    public void setCantMaximaPersonas(int cantMaximaPersonas) {
        this.cantMaximaPersonas = cantMaximaPersonas;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    public EstadoOperativo getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(EstadoOperativo estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }
}
