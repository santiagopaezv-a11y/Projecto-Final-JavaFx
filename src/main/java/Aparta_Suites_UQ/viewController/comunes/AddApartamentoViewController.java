package Aparta_Suites_UQ.viewController.comunes;
import Aparta_Suites_UQ.controller.comunes.AddApartamentoController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddApartamentoViewController implements Initializable {
    private final AddApartamentoController controller = new AddApartamentoController();
    @FXML
    private AnchorPane contenedorPrincipal;
    @FXML
    private Button buttom_savedApart;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TextField txtCapacidad;

    @FXML
    private TextField txtDormitorio;
    @FXML
    private TextField txtMotivo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecioNoche;
    @FXML
    private TextField txtCaracteristica;
    @FXML
    private void registrarMotivo(ActionEvent event){
            if (cbEstado.getValue().equals("Fuera de Servicio")){ txtMotivo.setVisible(true);
            }else {
                txtMotivo.setVisible(false);
            }
    }
    @FXML
    void guardarApartamento(ActionEvent event) {
        try {
            controller.guardarApartamento(
                    txtNombre.getText(),
                    txtDormitorio.getText(),
                    txtCapacidad.getText(),
                    txtCaracteristica.getText(),
                    txtPrecioNoche.getText(),
                    cbEstado.getValue(),
                    txtMotivo.getText());
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
            return;
        }
        Stage stageActual = (Stage) buttom_savedApart.getScene().getWindow();
        stageActual.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                "Pendiente", "Preparado", "Ocupado", "Fuera de Servicio", "En preparacion"
        );
        cbEstado.setItems(opciones);
        Apartamento aptAEditar = controller.getApartamentoEnEdicion();

        if (aptAEditar != null) {
            txtNombre.setText(aptAEditar.getNombre());
            txtDormitorio.setText(String.valueOf(aptAEditar.getNumeroDormitorios()));
            txtCapacidad.setText(String.valueOf(aptAEditar.getCantMaximaPersonas()));
            txtCaracteristica.setText(aptAEditar.getCaracteristicas());
            txtPrecioNoche.setText(String.valueOf(aptAEditar.getPrecioNoche()));
        }

    }
}
