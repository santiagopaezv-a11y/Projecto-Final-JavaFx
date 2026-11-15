package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.controller.Informe;
import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Novedad;
import Aparta_Suites_UQ.model.transacciones.Pago;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class InformeServicio {

    public static final String OCUPACION = "Ocupación por apartamento";
    public static final String INGRESOS = "Ingresos por periodo";
    public static final String CANALES = "Reservas por canal";
    public static final String SALDOS = "Saldos pendientes";
    public static final String NOVEDADES = "Apartamentos con más novedades";

    private static InformeServicio instance;

    private InformeServicio() {
    }

    public static InformeServicio getInstance() {
        if (instance == null) {
            instance = new InformeServicio();
        }
        return instance;
    }

    public List<String> nombresDeInformes() {
        return List.of(OCUPACION, INGRESOS, CANALES, SALDOS, NOVEDADES);
    }
    public boolean requierePeriodo(String nombre) {
        return OCUPACION.equals(nombre) || INGRESOS.equals(nombre);
    }

    public Informe generar(String nombre, LocalDate desde, LocalDate hasta) {
        if (nombre == null) {
            throw new IllegalArgumentException("Seleccione un informe.");
        }
        switch (nombre) {
            case OCUPACION:
                validarPeriodo(desde, hasta);
                return ocupacionPorApartamento(desde, hasta);
            case INGRESOS:
                validarPeriodo(desde, hasta);
                return ingresosPorPeriodo(desde, hasta);
            case CANALES:
                return reservasPorCanal();
            case SALDOS:
                return saldosPendientes();
            case NOVEDADES:
                return apartamentosConMasNovedades();
            default:
                throw new IllegalArgumentException("Informe no reconocido: " + nombre);
        }
    }

    private void validarPeriodo(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Seleccione la fecha inicial y la final del periodo.");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial.");
        }
    }

    public Informe ocupacionPorApartamento(LocalDate desde, LocalDate hasta) {
        long totalNoches = ChronoUnit.DAYS.between(desde, hasta) + 1;
        List<List<String>> filas = new ArrayList<>();
        for (Apartamento apto : ApartamentoServicio.getInstance().listarApartamentos()) {
            long ocupadas = 0;
            long bloqueadas = 0;
            for (LocalDate noche = desde; !noche.isAfter(hasta); noche = noche.plusDays(1)) {
                final LocalDate n = noche;
                boolean ocupada = apto.getReservas().stream()
                        .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA
                                || r.getEstado() == EstadoReserva.EN_CURSO
                                || r.getEstado() == EstadoReserva.FINALIZADA)
                        .anyMatch(r -> !n.isBefore(r.getEstancia().getFechaEntrada())
                                && n.isBefore(r.getEstancia().getFechaSalida()));
                boolean bloqueada = apto.getBloqueos().stream()
                        .anyMatch(b -> !n.isBefore(b.getFechaInicio()) && n.isBefore(b.getFechaFin()));
                if (ocupada) {
                    ocupadas++;
                } else if (bloqueada) {
                    bloqueadas++;
                }
            }
            long libres = totalNoches - ocupadas - bloqueadas;
            double porcentaje = totalNoches == 0 ? 0 : ocupadas * 100.0 / totalNoches;
            filas.add(List.of(apto.getNombre(), String.valueOf(ocupadas), String.valueOf(bloqueadas),
                    String.valueOf(libres), String.format(Locale.US, "%.1f %%", porcentaje)));
        }
        return new Informe(OCUPACION + " (" + desde + " a " + hasta + ")",
                List.of("Apartamento", "Noches ocupadas", "Noches bloqueadas", "Noches libres", "% ocupación"), filas);
    }

    public Informe ingresosPorPeriodo(LocalDate desde, LocalDate hasta) {
        Map<MedioPago, double[]> porMedio = new EnumMap<>(MedioPago.class);
        for (MedioPago medio : MedioPago.values()) {
            porMedio.put(medio, new double[]{0, 0});
        }
        for (Reserva reserva : Empresa.getInstance().getListaReservas()) {
            for (Pago pago : reserva.getFolio().getPagos()) {
                LocalDate dia = pago.getFecha().toLocalDate();
                if (!dia.isBefore(desde) && !dia.isAfter(hasta)) {
                    double[] acumulado = porMedio.get(pago.getMedioPago());
                    acumulado[0] += 1;
                    acumulado[1] += pago.getValor();
                }
            }
        }
        List<List<String>> filas = new ArrayList<>();
        double total = 0;
        double movimientos = 0;
        for (Map.Entry<MedioPago, double[]> e : porMedio.entrySet()) {
            filas.add(List.of(e.getKey().name(), String.valueOf((long) e.getValue()[0]), dinero(e.getValue()[1])));
            movimientos += e.getValue()[0];
            total += e.getValue()[1];
        }
        filas.add(List.of("TOTAL", String.valueOf((long) movimientos), dinero(total)));
        return new Informe(INGRESOS + " (" + desde + " a " + hasta + ")",
                List.of("Medio de pago", "Movimientos", "Ingresos"), filas);
    }

    /* Reservas de cada canal por estado, incluidas las canceladas y las no-show, y la comisión del canal Externo. */
    public Informe reservasPorCanal() {
        List<String> columnas = new ArrayList<>();
        columnas.add("Canal");
        for (EstadoReserva estado : EstadoReserva.values()) {
            columnas.add(estado.name());
        }
        columnas.add("Total");
        columnas.add("Comisión");
        List<List<String>> filas = new ArrayList<>();
        for (CanalReserva canal : CanalReserva.values()) {
            List<Reserva> delCanal = Empresa.getInstance().getListaReservas().stream()
                    .filter(r -> r.getCanal() == canal).collect(Collectors.toList());
            List<String> fila = new ArrayList<>();
            fila.add(canal.name());
            for (EstadoReserva estado : EstadoReserva.values()) {
                fila.add(String.valueOf(delCanal.stream().filter(r -> r.getEstado() == estado).count()));
            }
            fila.add(String.valueOf(delCanal.size()));
            double comision = delCanal.stream()
                    .filter(r -> r.getEstado() != EstadoReserva.CANCELADA && r.getEstado() != EstadoReserva.NO_SHOW)
                    .mapToDouble(Reserva::calcularComision).sum();
            fila.add(dinero(comision));
            filas.add(fila);
        }
        return new Informe(CANALES, columnas, filas);
    }

    /* Reservas (no canceladas) cuyo folio tiene saldo a favor del alojamiento, de mayor a menor. */
    public Informe saldosPendientes() {
        List<Reserva> conSaldo = Empresa.getInstance().getListaReservas().stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .filter(r -> r.getFolio().getSaldo() > 0)
                .sorted(Comparator.comparingDouble((Reserva r) -> r.getFolio().getSaldo()).reversed())
                .collect(Collectors.toList());
        List<List<String>> filas = new ArrayList<>();
        for (Reserva r : conSaldo) {
            filas.add(List.of(r.getCodigo(), r.getTitular().getNombre(), r.getApartamento().getNombre(),
                    r.getEstado().name(), dinero(r.getFolio().getTotalCargos()), dinero(r.getFolio().getTotalPagos()),
                    dinero(r.getFolio().getSaldo())));
        }
        double total = conSaldo.stream().mapToDouble(r -> r.getFolio().getSaldo()).sum();
        filas.add(List.of("TOTAL", "", "", "", "", "", dinero(total)));
        return new Informe(SALDOS, List.of("Reserva", "Titular", "Apartamento", "Estado", "Cargos", "Pagos", "Saldo"), filas);
    }

    public Informe apartamentosConMasNovedades() {
        List<Novedad> novedades = Empresa.getInstance().getListaNovedades();
        List<Apartamento> aptos = new ArrayList<>(ApartamentoServicio.getInstance().listarApartamentos());
        aptos.sort(Comparator.comparingLong((Apartamento a) ->
                novedades.stream().filter(n -> n.getApartamento() == a).count()).reversed());
        List<List<String>> filas = new ArrayList<>();
        for (Apartamento apto : aptos) {
            List<Novedad> deApto = novedades.stream().filter(n -> n.getApartamento() == apto).collect(Collectors.toList());
            String ultima = deApto.isEmpty() ? "-" : deApto.get(deApto.size() - 1).getDescripcion();
            filas.add(List.of(apto.getNombre(), String.valueOf(deApto.size()), ultima));
        }
        return new Informe(NOVEDADES, List.of("Apartamento", "Novedades", "Última novedad"), filas);
    }

    private String dinero(double valor) {
        return String.format(Locale.of("es", "CO"), "$%,.0f", valor);
    }
}
