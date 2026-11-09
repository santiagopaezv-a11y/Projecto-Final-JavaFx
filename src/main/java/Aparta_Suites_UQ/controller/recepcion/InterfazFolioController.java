package Aparta_Suites_UQ.controller.recepcion;

import Aparta_Suites_UQ.controller.servicio.FolioServicio;
import Aparta_Suites_UQ.controller.servicio.ReservaServicio;
import Aparta_Suites_UQ.model.reserva.Folio;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import java.util.List;

public class InterfazFolioController {

    private final ReservaServicio reservaServicio = ReservaServicio.getInstance();
    private final FolioServicio folioServicio = FolioServicio.getInstance();

    public List<Reserva> listarReservas() {
        return reservaServicio.listarReservas();
    }


    public Reserva getReservaPreseleccionada() {
        return reservaServicio.getReservaEditar();
    }



    public void agregarCargo(Folio folio, String concepto, String valor) {
        folioServicio.agregarCargo(folio, concepto, valor);
    }

    public void registrarPago(Folio folio, MedioPago medio, String valor) {
        folioServicio.registrarPago(folio, medio, valor);
    }

    public void registrarCorreccionCargo(Folio folio, String motivo, String valor) {
        folioServicio.registrarCorreccionCargo(folio, motivo, valor);
    }

    public void registrarCorreccionPago(Folio folio, MedioPago medio, String valor) {
        folioServicio.registrarCorreccionPago(folio, medio, valor);
    }
}
