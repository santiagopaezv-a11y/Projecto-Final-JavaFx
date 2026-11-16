package Aparta_Suites_UQ.controller.comunes;
import Aparta_Suites_UQ.controller.servicio.ExternaServicio;
import Aparta_Suites_UQ.model.reserva.ConflictoCanal;
import java.util.List;
public class ConflictosCanalController {
    public List<ConflictoCanal> listarConflictos() {
        return ExternaServicio.getInstance().listarConflictos();
    }
}
