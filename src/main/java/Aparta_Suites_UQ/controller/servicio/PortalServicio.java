package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.controller.BusquedaPortal;
import Aparta_Suites_UQ.controller.Cotizacion;
import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.NocheLiquidada;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.reserva.ResultadoCancelacion;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PortalServicio {
    private static PortalServicio instance;
    private BusquedaPortal ultimaBusqueda;
    private PortalServicio() {
    }
    public static PortalServicio getInstance() {
        if (instance == null) {
            instance = new PortalServicio();
        }
        return instance;
    }
    public BusquedaPortal getUltimaBusqueda() {
        return ultimaBusqueda;
    }
    public void setUltimaBusqueda(BusquedaPortal busqueda) {
        this.ultimaBusqueda = busqueda;
    }
    public Huesped getHuespedActual() {
        return SesionServicio.getInstance().getHuespedActual();
    }
    public Huesped identificarPorDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("Escriba su número de documento.");
        }
        Huesped huesped = Empresa.getInstance().buscarHuesped(documento);
        SesionServicio.getInstance().setHuespedActual(huesped);
        return huesped;
    }
    public Huesped guardarPerfil(String nombre, String documento, LocalDate nacimiento, String telefono, String correo) {
        if (vacio(nombre) || vacio(documento) || nacimiento == null || vacio(telefono) || vacio(correo)) {
            throw new IllegalArgumentException("Complete todos los datos del perfil.");
        }
        if (nacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        Huesped huesped = Empresa.getInstance().buscarHuesped(documento);
        if (huesped == null) {
            huesped = new Huesped(nombre.trim(), documento.trim(), nacimiento, telefono.trim(), correo.trim());
            Empresa.getInstance().getListaHuespedes().add(huesped);
        } else {
            huesped.setNombre(nombre.trim());
            huesped.setFechaNacimiento(nacimiento);
            huesped.setTelefono(telefono.trim());
            huesped.setCorreo(correo.trim());
        }
        SesionServicio.getInstance().setHuespedActual(huesped);
        return huesped;
    }
    public void cerrarPerfil() {
        SesionServicio.getInstance().setHuespedActual(null);
    }

    public List<Apartamento> buscarDisponibles(LocalDate entrada, LocalDate salida, int personas) {
        return ApartamentoServicio.getInstance().buscarDisponibles(entrada, salida, personas);
    }
    public Cotizacion cotizar(Apartamento apartamento, LocalDate entrada, LocalDate salida, int personas, int menores) {
        if (apartamento == null) {
            throw new IllegalArgumentException("Seleccione un apartamento.");
        }
        if (personas <= 0 || menores < 0 || menores > personas - 1) {
            throw new IllegalArgumentException(
                    "Revise las personas: debe haber al menos un adulto que paga (el titular).");
        }
        if (personas > apartamento.getCantMaximaPersonas()) {
            throw new IllegalArgumentException("Capacidad excedida: el apartamento admite máximo "
                    + apartamento.getCantMaximaPersonas() + " personas.");
        }
        Estancia estancia = new Estancia(entrada, salida);
        List<Ocupante> ocupantes = new ArrayList<>();
        for (int i = 0; i < personas - 1; i++) {
            boolean menor = i < menores;
            ocupantes.add(new Ocupante("Persona " + (i + 2), entrada.minusYears(menor ? 3 : 30)));
        }
        List<NocheLiquidada> noches = estancia.desglosarNoches(apartamento, ocupantes,
                Empresa.getInstance().getListaTemporadas());
        int total = noches.stream().mapToInt(NocheLiquidada::subtotal).sum();
        int anticipo = CanalReserva.PORTAL.calcularAnticipoRequerido(total);
        return new Cotizacion(apartamento, entrada, salida, personas, personas - menores, noches, total, anticipo);
    }
    public Reserva crearReservaPortal(Apartamento apartamento, LocalDate entrada, LocalDate salida,
                                      List<Ocupante> ocupantes) {
        Huesped titular = getHuespedActual();
        if (titular == null) {
            throw new IllegalArgumentException("Primero identifíquese o registre su perfil.");
        }
        List<Ocupante> lista = ocupantes == null ? List.of() : ocupantes;
        DatosReserva datos = new DatosReserva(titular.getNombre(), titular.getDocumento(), titular.getTelefono(),
                titular.getCorreo(), titular.getFechaNacimiento(), entrada, salida, apartamento,
                String.valueOf(lista.size()), "0", null);
        ReservaServicio reservas = ReservaServicio.getInstance();
        Reserva enEdicion = reservas.getReservaEditar();
        reservas.setReservaEditar(null);
        try {
            reservas.validarDatos(datos);
            return reservas.crearReserva(CanalReserva.PORTAL, null, datos, lista);
        } finally {
            reservas.setReservaEditar(enEdicion);
        }
    }
    public List<Reserva> listarMisReservas() {
        Huesped huesped = getHuespedActual();
        if (huesped == null) {
            return List.of();
        }
        return ReservaServicio.getInstance().refrescarEstados().stream()
                .filter(r -> r.getTitular() != null && r.getTitular().getDocumento().equalsIgnoreCase(huesped.getDocumento()))
                .sorted(Comparator.comparing(Reserva::getFechaCreacion).reversed())
                .collect(Collectors.toList());
    }
    public boolean pagar(Reserva reserva, MedioPago medio, String valorTexto) {
        if (reserva == null) {
            throw new IllegalArgumentException("Seleccione una reserva.");
        }
        if (!reserva.getEstado().esActiva()) {
            throw new IllegalArgumentException("Solo se pueden pagar reservas activas (Pendiente, Confirmada o En curso).");
        }
        if (medio == null) {
            throw new IllegalArgumentException("Seleccione el medio de pago.");
        }
        double valor;
        try {
            valor = Double.parseDouble(valorTexto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("El valor del pago debe ser numérico.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor del pago debe ser mayor a cero.");
        }
        if (valor > reserva.getFolio().getSaldo()) {
            throw new IllegalArgumentException("El pago no puede superar el saldo pendiente ($"
                    + (long) reserva.getFolio().getSaldo() + ").");
        }
        reserva.getFolio().registrarPago(new Pago(valor, medio));
        if (reserva.getEstado() == EstadoReserva.PENDIENTE
                && reserva.getFolio().getTotalPagos() >= reserva.calcularAnticipoRequerido()) {
            reserva.confirmar();
            return true;
        }
        return false;
    }
    public ResultadoCancelacion previsualizarCancelacion(Reserva reserva) {
        if (reserva == null || !reserva.puedeCancelarse()) {
            throw new IllegalArgumentException("Solo se puede cancelar una reserva Pendiente o Confirmada.");
        }
        return reserva.calcularResultadoCancelacion();
    }

    public ResultadoCancelacion cancelar(Reserva reserva) {
        return ReservaServicio.getInstance().cancelarReserva(reserva);
    }
    private boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
