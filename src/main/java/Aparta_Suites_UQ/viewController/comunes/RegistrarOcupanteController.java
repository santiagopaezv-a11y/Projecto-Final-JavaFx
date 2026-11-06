package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.utils.Navegacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegistrarOcupanteController {

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
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        LocalDate fechaNacimiento = dateFechaNacimiento.getValue();

        if (nombre.isEmpty() || fechaNacimiento == null) {
            Navegacion.enviarAlertaWarning("Por favor ingrese el nombre y la fecha de nacimiento del ocupante.");
            return;
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            Navegacion.enviarAlertaWarning("La fecha de nacimiento no puede ser posterior a la fecha actual.");
            return;
        }

        // Instanciar el objeto Ocupante con sus 2 datos
        this.ocupanteCreado = new Ocupante(nombre, fechaNacimiento);

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