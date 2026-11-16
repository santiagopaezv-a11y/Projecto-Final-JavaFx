package Aparta_Suites_UQ;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.*;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;

/**
 * Clase para probar las funcionalidades de empresa
 *
 * @author Erick Casallas Y Santiago Paez
 *
 * @since 2026-11
 * Licencia GNU/GPL V3.0
 * (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE)
 */
public class EmpresaTest {

    /**
     * Instancia para el manejo de logs
     */
    private static final Logger LOG = Logger.getLogger(EmpresaTest.class.getName());

    private Empresa empresa;
    private Apartamento apartamento;
    private Huesped titular;
    private LocalDate hoy;

    @BeforeEach
    public void setUp() {
        hoy = LocalDate.now();
        Empresa.setInstance(new Empresa("ApartaSuites_UQ", "956412", "Cra 25 #17N-32"));
        empresa = Empresa.getInstance();
        Reserva.setCantidadReserva(0);
        Apartamento.setNumeroApartamentos(0);

        apartamento = new Apartamento("Suite Premium", 1, 3, "Balcón y cocina", 100000, EstadoOperativo.PREPARADO);
        empresa.getListaApartamentos().add(apartamento);

        titular = new Huesped("Erick Casallas", "1188963354", LocalDate.of(2008, 9, 5), "3219103904", "erickcasallas@uqvirtual.edu.co");
        empresa.getListaHuespedes().add(titular);
    }

    @Test
    public void testDobleVenta() {
        LOG.info("Inicio de prueba testDobleVenta");

        Estancia estanciaUno = new Estancia(hoy.plusDays(5), hoy.plusDays(8));
        Reserva reservaUno = new Reserva(estanciaUno, CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 300000);
        apartamento.agregarReserva(reservaUno);

        // Se cruza una noche con reservaUno
        Estancia estanciaDos = new Estancia(hoy.plusDays(7), hoy.plusDays(10));
        Reserva reservaDos = new Reserva("E01", estanciaDos, CanalReserva.EXTERNA, titular, apartamento, new ArrayList<>(), 300000);

        assertFalse(apartamento.estaDisponible(hoy.plusDays(7), hoy.plusDays(10)));
        assertThrows(IllegalStateException.class, () -> apartamento.agregarReserva(reservaDos));
        assertTrue(apartamento.estaDisponible(hoy.plusDays(8), hoy.plusDays(10)));

        LOG.info("Fin de prueba testDobleVenta");
    }

    @Test
    public void testCapacidadMaxima() {
        LOG.info("Inicio de prueba testCapacidadMaxima");

        List<Ocupante> ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Santiago Paez", LocalDate.of(2000, 8, 15)));
        ocupantes.add(new Ocupante("Jhan Carlos", LocalDate.of(2000, 3, 22)));
        ocupantes.add(new Ocupante("Patrick Jane", LocalDate.of(2000, 11, 5)));

        // Titular + 3 ocupantes
        Reserva reserva = new Reserva(new Estancia(hoy.plusDays(2), hoy.plusDays(4)), CanalReserva.DIRECTA, titular, apartamento, ocupantes, 400000);

        assertEquals(4, reserva.getCantidadOcupantes());
        assertTrue(reserva.getCantidadOcupantes() > apartamento.getCantMaximaPersonas());

        LOG.info("Fin de prueba testCapacidadMaxima");
    }

    @Test
    public void testFechasEstancia() {
        LOG.info("Inicio de prueba testFechasEstancia");

        assertThrows(IllegalArgumentException.class, () -> new Estancia(hoy.plusDays(3), hoy.plusDays(3)));
        assertThrows(IllegalArgumentException.class, () -> new Estancia(hoy.plusDays(4), hoy.plusDays(2)));

        Estancia estancia = new Estancia(hoy.plusDays(1), hoy.plusDays(2));
        assertEquals(1, estancia.getNoches());

        LOG.info("Fin de prueba testFechasEstancia");
    }

    @Test
    public void testTarifaPorTemporadaYEdad() {
        LOG.info("Inicio de prueba testTarifaPorTemporadaYEdad");

        LocalDate entrada = hoy.plusDays(10);
        LocalDate salida = hoy.plusDays(12);
        Estancia estancia = new Estancia(entrada, salida);

        List<Temporada> temporadas = new ArrayList<>();
        temporadas.add(new Temporada(TipoTemporada.ALTA, hoy.plusDays(11), hoy.plusDays(15)));

        // menor a 6 no paga
        List<Ocupante> ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Santiago Paez", entrada.minusYears(6)));
        ocupantes.add(new Ocupante("Jhan Carlos", entrada.minusYears(6).plusDays(1)));

        assertEquals(500000, estancia.calcularValorTotal(apartamento, ocupantes, temporadas));

        LOG.info("Fin de prueba testTarifaPorTemporadaYEdad");
    }

    @Test
    public void testBloqueoMantenimiento() {
        LOG.info("Inicio de prueba testBloqueoMantenimiento");

        Bloqueo bloqueo = new Bloqueo(apartamento, hoy.plusDays(4), hoy.plusDays(6), "Mantenimiento");
        apartamento.agregarBloqueo(bloqueo);

        assertFalse(apartamento.estaDisponible(hoy.plusDays(4), hoy.plusDays(5)));

        Reserva reserva = new Reserva(new Estancia(hoy.plusDays(4), hoy.plusDays(6)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        assertThrows(IllegalStateException.class, () -> apartamento.agregarReserva(reserva));

        LOG.info("Fin de prueba testBloqueoMantenimiento");
    }

    @Test
    public void testEstadosReserva() {
        LOG.info("Inicio de prueba testEstadosReserva");

        Reserva reserva = new Reserva(new Estancia(hoy.plusDays(2), hoy.plusDays(4)), CanalReserva.PORTAL, titular, apartamento, new ArrayList<>(), 200000);
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());

        assertThrows(IllegalStateException.class, () -> reserva.cambiarEstado(EstadoReserva.EN_CURSO));
        assertThrows(IllegalStateException.class, () -> reserva.cambiarEstado(EstadoReserva.FINALIZADA));
        assertThrows(IllegalStateException.class, () -> reserva.cambiarEstado(EstadoReserva.NO_SHOW));
        assertThrows(IllegalStateException.class, reserva::confirmar);

        reserva.getFolio().registrarPago(new Pago(reserva.calcularAnticipoRequerido(), MedioPago.TRANSFERENCIA));
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());

        LOG.info("Fin de prueba testEstadosReserva");
    }

    @Test
    public void testVencimientoReservaPendiente() {
        LOG.info("Inicio de prueba testVencimientoReservaPendiente");

        LocalDate entrada = hoy.plusDays(5);
        LocalDate salida = hoy.plusDays(7);
        Reserva reserva = new Reserva(new Estancia(entrada, salida), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        apartamento.agregarReserva(reserva);

        // Simulamos que pasaron 25 horas
        reserva.setFechaCreacion(LocalDateTime.now().minusHours(25));
        reserva.actualizarEstadosAutomaticos();

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(apartamento.estaDisponible(entrada, salida));

        LOG.info("Fin de prueba testVencimientoReservaPendiente");
    }

    @Test
    public void testCheckIn() {
        LOG.info("Inicio de prueba testCheckIn");

        Reserva reserva = new Reserva(new Estancia(hoy, hoy.plusDays(2)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        reserva.getEstancia().setFechaEntrada(hoy.minusDays(1));

        assertThrows(IllegalStateException.class, reserva::registrarLlegada);

        reserva.confirmar();

        apartamento.setEstadoOperativo(EstadoOperativo.EN_PREPARACION);
        assertThrows(IllegalStateException.class, reserva::registrarLlegada);

        apartamento.setEstadoOperativo(EstadoOperativo.PREPARADO);
        reserva.registrarLlegada();

        assertEquals(EstadoReserva.EN_CURSO, reserva.getEstado());
        assertEquals(EstadoOperativo.OCUPADO, apartamento.getEstadoOperativo());

        LOG.info("Fin de prueba testCheckIn");
    }

    @Test
    public void testCheckOutYTiempoPreparacion() {
        LOG.info("Inicio de prueba testCheckOutYTiempoPreparacion");

        Reserva reservaUno = new Reserva(new Estancia(hoy, hoy.plusDays(2)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        reservaUno.getEstancia().setFechaEntrada(hoy.minusDays(1));
        reservaUno.confirmar();
        reservaUno.registrarLlegada();

        assertEquals(11, reservaUno.getEstancia().getFechaHoraSalida().getHour());
        assertEquals(15, reservaUno.getEstancia().getFechaHoraEntrada().getHour());

        Reserva reservaDos = new Reserva(new Estancia(hoy.plusDays(2), hoy.plusDays(4)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        assertTrue(reservaUno.cumpleTiempoPreparacion(reservaDos));

        reservaUno.hacerCheckOut();
        assertEquals(EstadoReserva.FINALIZADA, reservaUno.getEstado());
        assertEquals(EstadoOperativo.PENDIENTE, apartamento.getEstadoOperativo());

        LOG.info("Fin de prueba testCheckOutYTiempoPreparacion");
    }

    @Test
    public void testCancelacionYNoShow() {
        LOG.info("Inicio de prueba testCancelacionYNoShow");

        Huesped huespedDos = new Huesped("Patrick Jane", "1094561928", LocalDate.of(2000, 9, 18), "3207481920", "patrickjane@uqvirtual.edu.co");
        empresa.getListaHuespedes().add(huespedDos);

        // Con más de 7 días devuelve completo
        Reserva reservaUno = new Reserva(new Estancia(hoy.plusDays(10), hoy.plusDays(12)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        reservaUno.getFolio().registrarPago(new Pago(200000, MedioPago.TRANSFERENCIA));
        reservaUno.confirmar();
        reservaUno.cancelar();
        assertEquals(0, reservaUno.getResultadoCancelacion().getMontoRetenido());
        assertEquals(200000, reservaUno.getResultadoCancelacion().getMontoADevolver());

        // Entre 2 y 7 días retiene la mitad
        Reserva reservaDos = new Reserva(new Estancia(hoy.plusDays(4), hoy.plusDays(6)), CanalReserva.DIRECTA, huespedDos, apartamento, new ArrayList<>(), 200000);
        reservaDos.getFolio().registrarPago(new Pago(200000, MedioPago.TARJETA));
        reservaDos.confirmar();
        reservaDos.cancelar();
        assertEquals(100000, reservaDos.getResultadoCancelacion().getMontoRetenido());
        assertEquals(100000, reservaDos.getResultadoCancelacion().getMontoADevolver());

        // No-show retiene el total
        Reserva reservaTres = new Reserva(new Estancia(hoy.plusDays(1), hoy.plusDays(3)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        reservaTres.getFolio().registrarPago(new Pago(200000, MedioPago.EFECTIVO));
        reservaTres.confirmar();
        reservaTres.declararNoShow();
        assertEquals(EstadoReserva.NO_SHOW, reservaTres.getEstado());
        assertEquals(200000, reservaTres.getResultadoCancelacion().getMontoRetenido());
        assertEquals(0, reservaTres.getResultadoCancelacion().getMontoADevolver());

        LOG.info("Fin de prueba testCancelacionYNoShow");
    }

    @Test
    public void testValorYPoliticaCongelados() {
        LOG.info("Inicio de prueba testValorYPoliticaCongelados");

        Reserva reserva = new Reserva(new Estancia(hoy.plusDays(5), hoy.plusDays(7)), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 200000);
        String politicaInicial = reserva.getPoliticaCancelacionAplicada();

        apartamento.setPrecioNoche(250000);
        empresa.setPoliticaCancelacion(new PoliticaCancelacion(15, 5, 0.70));

        assertEquals(200000, reserva.getValorAlojamientoCongelado());
        assertEquals(politicaInicial, reserva.getPoliticaCancelacionAplicada());

        LOG.info("Fin de prueba testValorYPoliticaCongelados");
    }

    @Test
    public void testFolioInalterable() {
        LOG.info("Inicio de prueba testFolioInalterable");

        Folio folio = new Folio();
        folio.agregarCargo(new Cargo("Minibar", 50000));
        folio.registrarPago(new Pago(50000, MedioPago.EFECTIVO));

        folio.getCargos().clear();
        folio.getPagos().clear();
        assertEquals(1, folio.getCargos().size());
        assertEquals(1, folio.getPagos().size());

        folio.registrarCorreccionCargo("Cobro errado", -50000);
        folio.registrarCorreccionPago(-50000, MedioPago.EFECTIVO);

        assertEquals(2, folio.getCargos().size());
        assertEquals(2, folio.getPagos().size());
        assertEquals(0, folio.getSaldo());

        LOG.info("Fin de prueba testFolioInalterable");
    }

    @Test
    public void testReservasExternasYConflictos() {
        LOG.info("Inicio de prueba testReservasExternasYConflictos");

        LocalDate entrada = hoy.plusDays(6);
        LocalDate salida = hoy.plusDays(9);

        Huesped huespedDos = new Huesped("Jhan Carlos", "1094382910", LocalDate.of(2000, 6, 12), "3184920183", "jhancarlos@uqvirtual.edu.co");
        empresa.getListaHuespedes().add(huespedDos);

        Reserva reservaUno = new Reserva(new Estancia(entrada, salida), CanalReserva.DIRECTA, titular, apartamento, new ArrayList<>(), 300000);
        apartamento.agregarReserva(reservaUno);

        Reserva reservaDos = new Reserva("E01", new Estancia(entrada.plusDays(1), salida), CanalReserva.EXTERNA, huespedDos, apartamento, new ArrayList<>(), 200000);

        assertThrows(IllegalStateException.class, () -> apartamento.agregarReserva(reservaDos));
        empresa.getListaConflictos().add(new ConflictoCanal("E01", CanalReserva.EXTERNA.name(), "Choque con reserva " + reservaUno.getCodigo(), LocalDateTime.now()));

        assertEquals(1, empresa.getListaConflictos().size());
        assertTrue(empresa.getListaReservas().contains(reservaUno));
        assertFalse(empresa.getListaReservas().contains(reservaDos));

        Reserva reservaTres = new Reserva("E02", new Estancia(hoy.plusDays(15), hoy.plusDays(17)), CanalReserva.EXTERNA, huespedDos, apartamento, new ArrayList<>(), 200000);
        apartamento.agregarReserva(reservaTres);

        assertTrue(empresa.existeCodigoExterno("E02"));
        assertTrue(apartamento.existeCodigoExterno("e02"));

        LOG.info("Fin de prueba testReservasExternasYConflictos");
    }
}