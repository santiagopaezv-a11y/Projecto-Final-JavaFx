package Aparta_Suites_UQ.viewController.comunes;
import Aparta_Suites_UQ.controller.comunes.RegistrarOcupanteController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrarOcupanteViewController {
    private final RegistrarOcupanteController controller = new RegistrarOcupanteController();

    @FXML private TextField txtNombre;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Ocupante ocupanteCreado;

    @FXML
    public void initialize() {
        ocupanteCreado = null;
    }

    @FXML
    void onGuardar(ActionEvent event) {
        try {
            this.ocupanteCreado = controller
                    .crearOcupante(txtNombre.getText(), dateFechaNacimiento.getValue());
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
            return;
        }
        cerrarVentana();
    }

    @FXML
    void onCancelar(ActionEvent event) {
        this.ocupanteCreado = null;
        cerrarVentana();
    }

    public Ocupante getOcupanteCreado() {
        return ocupanteCreado;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}