package Aparta_Suites_UQ.controller.servicio;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.Empresa;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.ConflictoCanal;
import Aparta_Suites_UQ.model.reserva.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class ExternaServicio {

    private static final String[] PLATAFORMAS = {"Booking (simulada)", "Airbnb (simulada)", "Expedia (simulada)"};
    private static final String[] NOMBRES = {"Laura Giraldo", "Pedro Salazar", "Marta Osorio", "Camilo Rincón",
            "Sofía Henao", "Julián Bedoya", "Natalia Cardona", "Esteban Londoño"};
    private static ExternaServicio instance;
    private final Random azar = new Random();
    private ExternaServicio() {
    }
    public static ExternaServicio getInstance() {
        if (instance == null) {
            instance = new ExternaServicio();
        }
        return instance;
    }
    public List<ConflictoCanal> listarConflictos() {
        List<ConflictoCanal> lista = new ArrayList<>(Empresa.getInstance().getListaConflictos());
        Collections.reverse(lista);
        return lista;
    }
    public Reserva recibirReservaExterna(String plataforma, String codigoExterno, DatosReserva datos,
                                         List<Ocupante> ocupantes) {
        if (plataforma == null || plataforma.trim().isEmpty()) {
            throw new IllegalArgumentException("Indique la plataforma de origen.");
        }
        if (codigoExterno == null || codigoExterno.trim().isEmpty()) {
            throw new IllegalArgumentException("Indique el código de la reserva en la plataforma.");
        }
        String codigo = codigoExterno.trim();
        String origen = plataforma.trim();
        ReservaServicio reservas = ReservaServicio.getInstance();
        Reserva enEdicion = reservas.getReservaEditar();
        reservas.setReservaEditar(null);
        try {
            reservas.validarDatos(datos);
            if (Empresa.getInstance().existeCodigoExterno(codigo)) {
                registrarConflicto(codigo, origen, "El código externo ya generó otra reserva.");
                throw new IllegalArgumentException("Rechazada: el código externo " + codigo
                        + " ya generó otra reserva. Se registró como conflicto de canal.");
            }
            try {
                reservas.validarDisponibilidad(datos);
            } catch (IllegalArgumentException e) {
                registrarConflicto(codigo, origen, e.getMessage());
                throw new IllegalArgumentException("Rechazada y registrada como conflicto de canal: " + e.getMessage());
            }
            Reserva reserva = reservas.crearReserva(CanalReserva.EXTERNA, codigo, datos, ocupantes);
            reservas.confirmarReserva(reserva);
            return reserva;
        } finally {
            reservas.setReservaEditar(enEdicion);
        }
    }
    private void registrarConflicto(String codigo, String origen, String motivo) {
        Empresa.getInstance().getListaConflictos().add(new ConflictoCanal(codigo, origen, motivo, LocalDateTime.now()));
    }
    public String simularReservaExterna() {
        List<Apartamento> aptos = ApartamentoServicio.getInstance().listarApartamentos();
        if (aptos.isEmpty()) {
            throw new IllegalArgumentException("No hay apartamentos para simular reservas.");
        }
        Apartamento apto = aptos.get(azar.nextInt(aptos.size()));
        String plataforma = PLATAFORMAS[azar.nextInt(PLATAFORMAS.length)];
        String codigo = "EXT-" + (1000 + azar.nextInt(9000));
        List<Reserva> externas = new ArrayList<>();
        for (Reserva r : Empresa.getInstance().getListaReservas()) {
            if (r.getCodigoExterno() != null) {
                externas.add(r);
            }
        }
        if (!externas.isEmpty() && azar.nextInt(6) == 0) {
            codigo = externas.get(azar.nextInt(externas.size())).getCodigoExterno();
        }
        LocalDate entrada = LocalDate.now().plusDays(1 + azar.nextInt(45));
        LocalDate salida = entrada.plusDays(1 + azar.nextInt(4));
        int adicionales = azar.nextInt(Math.max(1, apto.getCantMaximaPersonas()));
        List<Ocupante> ocupantes = new ArrayList<>();
        for (int i = 0; i < adicionales; i++) {
            ocupantes.add(new Ocupante("Acompañante " + (i + 1), LocalDate.now().minusYears(2 + azar.nextInt(50))));
        }
        String nombre = NOMBRES[azar.nextInt(NOMBRES.length)];
        DatosReserva datos = new DatosReserva(nombre, String.valueOf(1000000000L + azar.nextInt(900000000)),
                "310" + (1000000 + azar.nextInt(8999999)), nombre.split(" ")[0].toLowerCase() + "@correo.com",
                LocalDate.of(1975 + azar.nextInt(25), 1 + azar.nextInt(12), 1 + azar.nextInt(28)),
                entrada, salida, apto, String.valueOf(adicionales), "0", null);
        try {
            Reserva reserva = recibirReservaExterna(plataforma, codigo, datos, ocupantes);
            return "Simulación: " + plataforma + " envió la reserva " + codigo + " (" + apto.getNombre() + ", "
                    + entrada + " a " + salida + "). Quedó registrada con el código " + reserva.getCodigo()
                    + " y una comisión de $" + reserva.calcularComision() + ".";
        } catch (IllegalArgumentException e) {
            return "Simulación: " + plataforma + " envió la reserva " + codigo + " (" + apto.getNombre() + ", "
                    + entrada + " a " + salida + ").\n" + e.getMessage();
        }
    }
}
