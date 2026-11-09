package Aparta_Suites_UQ.viewController.comunes;
import Aparta_Suites_UQ.controller.comunes.EditarEstadoApartamentoController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarEstadoApartamentoViewController {

    @FXML private TextField txtApartamentoNombre;
    @FXML private ComboBox<EstadoOperativo> cbEstadoOperativo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final EditarEstadoApartamentoController controller = new EditarEstadoApartamentoController();
    private Apartamento apartamentoSeleccionado;

    @FXML
    public void initialize() {
        cbEstadoOperativo.setItems(FXCollections.observableArrayList(EstadoOperativo.values()));

        apartamentoSeleccionado = controller.getApartamentoEnEdicion();
        if (apartamentoSeleccionado != null) {
            txtApartamentoNombre.setText(apartamentoSeleccionado.getNombre());
            cbEstadoOperativo.setValue(apartamentoSeleccionado.getEstadoOperativo());
        }
    }

    @FXML
    void onGuardarEstado(ActionEvent event) {
        try {
            controller.guardarEstado(cbEstadoOperativo.getValue());
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
            return;
        }

        if (apartamentoSeleccionado != null) {
            Vista.enviarAlertaInfo("Estado operativo del apartamento actualizado con éxito.");
        }

        cerrarVentana();
    }

    @FXML
    void onCancelar(ActionEvent event) {
        controller.cancelarEdicion();
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}