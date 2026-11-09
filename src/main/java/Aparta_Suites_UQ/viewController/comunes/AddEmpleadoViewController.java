package Aparta_Suites_UQ.viewController.comunes;
import Aparta_Suites_UQ.controller.comunes.AddEmpleadoController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.huesped.Empleado;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEmpleadoViewController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbRol;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;

    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final AddEmpleadoController controller = new AddEmpleadoController();
    private Empleado empleadoEdicion;

    @FXML
    public void initialize() {
        cargarRoles();

        empleadoEdicion = controller.getEmpleadoEnEdicion();

        if (empleadoEdicion != null) {
            lblTitulo.setText("Editar Empleado");
            cargarDatosEmpleado(empleadoEdicion);
        } else {
            lblTitulo.setText("Registrar Empleado");
        }
    }

    private void cargarRoles() {
        cbRol.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "RECEPCIONISTA"));
    }

    private void cargarDatosEmpleado(Empleado empleado) {
        txtDocumento.setText(empleado.getDocumento());
        txtNombre.setText(empleado.getNombre());
        dateFechaNacimiento.setValue(empleado.getFechaNacimiento());
        txtTelefono.setText(empleado.getTelefono());
        txtCorreo.setText(empleado.getCorreo());

        if (empleado.getRol() != null) {
            cbRol.setValue(empleado.getRol().toString());
        }

        if (empleado.getContrasena() != null) {
            txtPassword.setText(empleado.getContrasena());
        }
    }

    @FXML
    void onGuardarEmpleado(ActionEvent event) {
        try {
            boolean fueEdicion = controller.guardarEmpleado(
                    txtDocumento.getText(),
                    txtNombre.getText(),
                    cbRol.getValue(),
                    dateFechaNacimiento.getValue(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    txtPassword.getText());
            Vista.enviarAlertaInfo(fueEdicion
                    ? "¡Empleado actualizado correctamente!"
                    : "¡Empleado registrado exitosamente!");
            cerrarVentana();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
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