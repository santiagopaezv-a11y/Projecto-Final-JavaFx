package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.huesped.Administrador;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Recepcionista;
import Aparta_Suites_UQ.utils.Navegacion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;

public class AddEmpleadoController {

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

    private Empleado empleadoEdicion;

    @FXML
    public void initialize() {
        cargarRoles();

        empleadoEdicion = Navegacion.getInstance().getEmpleadoEditar();

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
        if (!validarCampos()) {
            return;
        }
        String documento = txtDocumento.getText().trim();
        String nombre = txtNombre.getText().trim();
        String rolSeleccionado = cbRol.getValue();
        LocalDate fechaNacimiento = dateFechaNacimiento.getValue();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText().trim();

        if (empleadoEdicion != null) {
            // --- MODO EDICIÓN ---
            empleadoEdicion.setDocumento(documento);
            empleadoEdicion.setNombre(nombre);
            empleadoEdicion.setFechaNacimiento(fechaNacimiento);
            empleadoEdicion.setTelefono(telefono);
            empleadoEdicion.setCorreo(correo);
            empleadoEdicion.setContrasena(password);

            // Actualización del rol si el modelo lo permite dinámicamente mediante setter
            if (empleadoEdicion.getRol() != null) {
                // Si 'rol' es de un Enum tipo RolEmpleado:
                // empleadoEdicion.setRol(RolEmpleado.valueOf(rolSeleccionado));
            }

            Navegacion.getInstance().setEmpleadoEditar(null);
            Navegacion.enviarAlertaInfo("¡Empleado actualizado correctamente!");

        } else {
            // --- MODO CREACIÓN ---
            for (Empleado emp : Navegacion.getInstance().getListaEmpleados()) {
                if (emp.getDocumento().equalsIgnoreCase(documento)) {
                    Navegacion.enviarAlertaWarning("Ya existe un empleado registrado con el número de documento: " + documento);
                    return;
                }
            }

            // Instanciación según el rol seleccionado
            Empleado nuevoEmpleado;
            if ("ADMINISTRADOR".equalsIgnoreCase(rolSeleccionado)) {
                nuevoEmpleado = new Administrador(nombre, documento, fechaNacimiento, telefono, correo, password);
            } else {
                nuevoEmpleado = new Recepcionista(nombre, documento, fechaNacimiento, telefono, correo, password);
            }

            Navegacion.getInstance().getListaEmpleados().add(nuevoEmpleado);
            Navegacion.enviarAlertaInfo("¡Empleado registrado exitosamente!");
        }

        cerrarVentana();
    }

    private boolean validarCampos() {
        if (txtDocumento.getText().trim().isEmpty() ||
                txtNombre.getText().trim().isEmpty() ||
                cbRol.getValue() == null ||
                dateFechaNacimiento.getValue() == null ||
                txtTelefono.getText().trim().isEmpty() ||
                txtCorreo.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty()) {

            Navegacion.enviarAlertaWarning("Por favor complete todos los campos obligatorios (*).");
            return false;
        }

        Period edad = Period.between(dateFechaNacimiento.getValue(), LocalDate.now());
        if (edad.getYears() < 18) {
            Navegacion.enviarAlertaWarning("El empleado debe ser mayor de edad (18 años o más).");
            return false;
        }

        return true;
    }

    @FXML
    void onCancelar(ActionEvent event) {
        Navegacion.getInstance().setEmpleadoEditar(null);
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}