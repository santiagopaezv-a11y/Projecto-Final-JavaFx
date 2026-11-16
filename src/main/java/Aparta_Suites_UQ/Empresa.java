package Aparta_Suites_UQ;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import Aparta_Suites_UQ.model.huesped.Administrador;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.reserva.ConflictoCanal;
import Aparta_Suites_UQ.model.reserva.PoliticaCancelacion;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Novedad;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String nit;
    private String direccion;

    private static Empresa instance;

    private PoliticaCancelacion politicaCancelacion = PoliticaCancelacion.porDefecto();
    private List<Novedad> listaNovedades = new ArrayList<>();
    private List<ConflictoCanal> listaConflictos = new ArrayList<>();
    private Empleado empleadoSistema;
    private List<Apartamento> listaApartamentos = new ArrayList<>();
    private List<Reserva> listaReservas = new ArrayList<>();
    private List<Huesped> listaHuespedes = new ArrayList<>();
    private List<Temporada> listaTemporadas = new ArrayList<>();
    private List<Empleado> listaEmpleados = new ArrayList<>();

    public Empresa(String nombre, String nit, String direccion) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        cargarTemporadasPorDefecto();
    }

    protected Empresa() {
        this.nombre = "Aparta-Suites UQ";
        this.nit = "-";
        this.direccion = "Salento, Quindío";
        cargarTemporadasPorDefecto();
    }

    private void cargarTemporadasPorDefecto() {
        // 1. TEMPORADA ALTA (Diciembre - Enero)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 1, 31)
        ));

        // TEMPORADA ALTA (Semana Santa 2026)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 3, 29),
                LocalDate.of(2026, 4, 5)
        ));

        // TEMPORADA ALTA (Mitad de Año)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 6, 15),
                LocalDate.of(2026, 7, 15)
        ));

        // 2. TEMPORADA MEDIA (Ejemplo: Puente Festivo)
        listaTemporadas.add(new Temporada(
                TipoTemporada.MEDIA,
                LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 5, 18)
        ));
    }

    public static Empresa getInstance() {
        if (instance == null) {
            instance = new Empresa();
        }
        return instance;
    }

    /**
     * Reemplaza la instancia estática cuando los datos son cargados desde la persistencia.
     */
    public static void setInstance(Empresa nuevaEmpresa) {
        if (nuevaEmpresa != null) {
            instance = nuevaEmpresa;
        }
    }

    public List<Apartamento> getListaApartamentos() {
        return listaApartamentos;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public List<Huesped> getListaHuespedes() {
        return listaHuespedes;
    }

    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada != null) {
            this.listaTemporadas.add(temporada);
        }
    }

    public static boolean validarCredencialEmpleado(String user, String contrasena) {
        return getInstance().buscarEmpleado(user, contrasena) != null;
    }

    public Empleado buscarEmpleado(String usuario, String contrasena) {
        for (Empleado empleado : listaEmpleados) {
            if (empleado.validarCredenciales(usuario, contrasena)) {
                return empleado;
            }
        }
        return null;
    }

    /** Administrador interno que se usa como autor cuando quien opera entró con la cuenta fija del sistema. */
    public Empleado getEmpleadoSistema() {
        if (empleadoSistema == null) {
            empleadoSistema = new Administrador("Administrador del sistema", "0", LocalDate.of(1990, 1, 1),
                    "0", "admin@apartasuites.uq", "1234");
        }
        return empleadoSistema;
    }

    public Huesped buscarHuesped(String documento) {
        if (documento == null) {
            return null;
        }
        String doc = documento.trim();
        for (Huesped huesped : listaHuespedes) {
            if (huesped.getDocumento().equalsIgnoreCase(doc)) {
                return huesped;
            }
        }
        return null;
    }

    /** Un mismo código externo no puede crear dos reservas. */
    public boolean existeCodigoExterno(String codigoExterno) {
        if (codigoExterno == null || codigoExterno.trim().isEmpty()) {
            return false;
        }
        String codigo = codigoExterno.trim();
        return listaReservas.stream()
                .anyMatch(r -> r.getCodigoExterno() != null && r.getCodigoExterno().equalsIgnoreCase(codigo));
    }

    public PoliticaCancelacion getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    /** Solo afecta a las reservas que se creen después: las existentes conservan la suya. */
    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
        if (politicaCancelacion == null) {
            throw new IllegalArgumentException("La política de cancelación no puede ser nula.");
        }
        this.politicaCancelacion = politicaCancelacion;
    }
    public void sincronizarContadores() {
        // 1. Sincronizar contador de Reservas
        int maxReserva = listaReservas.stream()
                .mapToInt(r -> {
                    try {
                        return Integer.parseInt(r.getCodigo().replaceAll("[^0-9]", ""));
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        Reserva.setCantidadReserva(maxReserva);

        // 2. Sincronizar contador de Apartamentos (Usando el setter real de tu clase)
        int maxApto = listaApartamentos.stream()
                .mapToInt(a -> {
                    try {
                        return Integer.parseInt(a.getId().replaceAll("[^0-9]", ""));
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        Apartamento.setNumeroApartamentos(maxApto); // <-- ¡Aquí estaba comentado y con otro nombre!

        // 3. Sincronizar contador de Empleados (Asegúrate de usar getId() o getCodigo(), NO getDocumento())
        int maxEmpleado = listaEmpleados.stream()
                .mapToInt(e -> {
                    try {
                        return Integer.parseInt(e.getUsuario().replaceAll("[^0-9]", "")); // <-- Ajusta si se llama getCodigo()
                    } catch (Exception ex) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        Empleado.setCantidadEmpleado(maxEmpleado); // <-- Descomenta y pon el nombre exacto de tu setter en Empleado
    }

    public List<Novedad> getListaNovedades() {
        return listaNovedades;
    }

    public List<ConflictoCanal> getListaConflictos() {
        return listaConflictos;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}