package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.utils.Navegacion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarEstadoApartamentoController {

    @FXML private TextField txtApartamentoNombre;
    @FXML private ComboBox<EstadoOperativo> cbEstadoOperativo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Apartamento apartamentoSeleccionado;

    @FXML
    public void initialize() {
        cbEstadoOperativo.setItems(FXCollections.observableArrayList(EstadoOperativo.values()));

        apartamentoSeleccionado = Navegacion.getInstance().getApartamentoSeleccionadoParaEditar();
        if (apartamentoSeleccionado != null) {
            txtApartamentoNombre.setText(apartamentoSeleccionado.getNombre());
            cbEstadoOperativo.setValue(apartamentoSeleccionado.getEstadoOperativo());
        }
    }

    @FXML
    void onGuardarEstado(ActionEvent event) {
        if (cbEstadoOperativo.getValue() == null) {
            Navegacion.enviarAlertaWarning("Por favor seleccione un estado operativo.");
            return;
        }

        if (apartamentoSeleccionado != null) {
            apartamentoSeleccionado.setEstadoOperativo(cbEstadoOperativo.getValue());
            Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(null);
            Navegacion.enviarAlertaInfo("Estado operativo del apartamento actualizado con éxito.");
        }

        cerrarVentana();
    }

    @FXML
    void onCancelar(ActionEvent event) {
        Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(null);
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}