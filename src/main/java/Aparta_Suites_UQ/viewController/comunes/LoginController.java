package Aparta_Suites_UQ.viewController.comunes;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;

public class LoginController {

    @FXML
    private CustomTextField txtUsuario;

    @FXML
    private CustomPasswordField txtPassword;

    @FXML
    private CustomTextField txtPasswordVisible;

    @FXML
    private Button btnIngresar;

    @FXML
    private Label lblMensaje;

    private final ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    public void initialize() {
        configurarBotonLimpiarUsuario();
        configurarAlternarContrasena();
        configurarValidaciones();
    }

    private void configurarBotonLimpiarUsuario() {
        Button btnLimpiar = new Button("✕");
        btnLimpiar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 5 0 0; -fx-text-fill: #95a5a6;");
        btnLimpiar.setOnAction(e -> txtUsuario.clear());

        // Se muestra la X solo si el campo no está vacío
        btnLimpiar.visibleProperty().bind(txtUsuario.textProperty().isNotEmpty());
        txtUsuario.setRight(btnLimpiar);
    }

    private void configurarAlternarContrasena() {
        txtPassword.setRight(null);
        txtPasswordVisible.setRight(null);
        ToggleButton btnVerPassword = crearBotonOjo(false);
        ToggleButton btnOcultarPassword = crearBotonOjo(true);

        txtPassword.setRight(btnVerPassword);
        txtPasswordVisible.setRight(btnOcultarPassword);
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        btnVerPassword.setOnAction(e -> {
            txtPasswordVisible.setVisible(true);
            txtPassword.setVisible(false);
            btnOcultarPassword.setSelected(true);
            txtPasswordVisible.requestFocus();
            txtPasswordVisible.selectEnd();
        });

        // Comportamiento al ocultar contraseña
        btnOcultarPassword.setOnAction(e -> {
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
            btnVerPassword.setSelected(false);
            txtPassword.requestFocus();
            txtPassword.selectEnd();
        });
    }

    private ToggleButton crearBotonOjo(boolean seleccionadoPorDefecto) {
        ToggleButton botonOjo = new ToggleButton("👁");
        botonOjo.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 5 0 0;");
        botonOjo.setSelected(seleccionadoPorDefecto);
        return botonOjo;
    }

    private void configurarValidaciones() {
        validationSupport.registerValidator(txtUsuario, Validator.createEmptyValidator("El usuario es requerido"));
        validationSupport.registerValidator(txtPassword, Validator.createEmptyValidator("La contraseña es requerida"));
        validationSupport.setValidationDecorator(new CompoundValidationDecoration());
        btnIngresar.disableProperty().bind(validationSupport.invalidProperty());
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if ("sanglie".equals(usuario) && "123".equals(password)) {
            Navegacion.cambiarEscena(event, Paths.ADMINPANEL);
        } else if (Navegacion.validarCredencialEmpleado(usuario,password)){
            Navegacion.cambiarEscena(event, Paths.RECEPCION_PANEL);
        }  else {
            lblMensaje.setStyle("-fx-text-fill: #e74c3c;");
            lblMensaje.setText("Credenciales incorrectas.");
        }
    }
}
