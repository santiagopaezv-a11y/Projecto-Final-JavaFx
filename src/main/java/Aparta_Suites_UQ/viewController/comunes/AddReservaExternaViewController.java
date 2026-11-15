package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.controller.comunes.AddReservaExternaController;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.viewController.util.Tablas;
import Aparta_Suites_UQ.viewController.util.Vista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Formulario para recibir una reserva del canal Externo. Solo lee campos y muestra mensajes;
 * las reglas están en {@link AddReservaExternaController}.
 */
public class AddReservaExternaViewController {

    private final AddReservaExternaController controller = new AddReservaExternaController();
    private final ObservableList<Ocupante> ocupantes = FXCollections.observableArrayList();

    @FXML private ComboBox<String> cbPlataforma;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private DatePicker dpNacimiento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<Apartamento> cbApartamento;
    @FXML private DatePicker dpEntrada;
    @FXML private DatePicker dpSalida;
    @FXML private TextField txtOcNombre;
    @FXML private DatePicker dpOcNacimiento;
    @FXML private TableView<Ocupante> tblOcupantes;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {
        cbPlataforma.setItems(FXCollections.observableArrayList(controller.plataformas()));
        Tablas.mostrarNombres(cbApartamento);
        cbApartamento.setItems(FXCollections.observableArrayList(controller.listarApartamentos()));
        Tablas.columna(tblOcupantes, "Nombre", Ocupante::getNombre);
        Tablas.columna(tblOcupantes, "Nacimiento", o -> o.getFechaNacimiento().toString());
        Tablas.columna(tblOcupantes, "Edad", o -> String.valueOf(o.calcularEdad(java.time.LocalDate.now())));
        tblOcupantes.setItems(ocupantes);
    }

    @FXML
    void onAgregarOcupante(ActionEvent event) {
        try {
            ocupantes.add(controller.crearOcupante(txtOcNombre.getText(), dpOcNacimiento.getValue()));
            txtOcNombre.clear();
            dpOcNacimiento.setValue(null);
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onQuitarOcupante(ActionEvent event) {
        Ocupante seleccionado = tblOcupantes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            ocupantes.remove(seleccionado);
        }
    }

    @FXML
    void onRecibir(ActionEvent event) {
        DatosReserva datos = new DatosReserva(txtNombre.getText(), txtDocumento.getText(), txtTelefono.getText(),
                txtCorreo.getText(), dpNacimiento.getValue(), dpEntrada.getValue(), dpSalida.getValue(),
                cbApartamento.getValue(), String.valueOf(ocupantes.size()), "0", null);
        try {
            Reserva reserva = controller.recibir(cbPlataforma.getValue(), txtCodigo.getText(), datos,
                    new ArrayList<>(ocupantes));
            Vista.enviarAlertaInfo("Reserva externa recibida y confirmada con el código " + reserva.getCodigo()
                    + ". Comisión para la plataforma: " + Tablas.dinero(reserva.calcularComision()) + ".");
            cerrar();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }
}
