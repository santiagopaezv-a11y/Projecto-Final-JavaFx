package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.controller.servicio.ApartamentoServicio;
import Aparta_Suites_UQ.controller.servicio.ExternaServicio;
import Aparta_Suites_UQ.controller.servicio.OcupanteServicio;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.Reserva;
import java.time.LocalDate;
import java.util.List;

public class AddReservaExternaController {
    public List<String> plataformas() {
        return List.of("Booking (simulada)", "Airbnb (simulada)", "Expedia (simulada)", "Otra plataforma");
    }
    public List<Apartamento> listarApartamentos() {
        return ApartamentoServicio.getInstance().listarApartamentos();
    }

    public Ocupante crearOcupante(String nombre, LocalDate nacimiento) {
        return OcupanteServicio.getInstance().crearOcupante(nombre, nacimiento);
    }
    public Reserva recibir(String plataforma, String codigoExterno, DatosReserva datos, List<Ocupante> ocupantes) {
        return ExternaServicio.getInstance().recibirReservaExterna(plataforma, codigoExterno, datos, ocupantes);
    }
}
