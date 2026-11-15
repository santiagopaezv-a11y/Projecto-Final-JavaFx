package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.controller.comunes.ConflictosCanalController;
import Aparta_Suites_UQ.model.reserva.ConflictoCanal;
import Aparta_Suites_UQ.viewController.util.Tablas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * Ventana con los conflictos de canal registrados.
 */
public class ConflictosCanalViewController {

    private final ConflictosCanalController controller = new ConflictosCanalController();

    @FXML private TableView<ConflictoCanal> tblConflictos;
    @FXML private Button btnCerrar;

    @FXML
    public void initialize() {
        Tablas.columna(tblConflictos, "Fecha", c -> c.getFechaRegistro().toLocalDate() + " " + c.getFechaRegistro().toLocalTime().withNano(0));
        Tablas.columna(tblConflictos, "Plataforma", ConflictoCanal::getCanalOrigen);
        Tablas.columna(tblConflictos, "Código externo", ConflictoCanal::getCodigoExterno);
        Tablas.columna(tblConflictos, "Motivo", ConflictoCanal::getMotivo);
        tblConflictos.setItems(FXCollections.observableArrayList(controller.listarConflictos()));
    }

    @FXML
    void onCerrar(ActionEvent event) {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
}
