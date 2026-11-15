package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.controller.comunes.NovedadesController;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Novedad;
import Aparta_Suites_UQ.viewController.util.Tablas;
import Aparta_Suites_UQ.viewController.util.Vista;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Ventana para registrar y consultar novedades de los apartamentos.
 */
public class NovedadesViewController {

    private final NovedadesController controller = new NovedadesController();

    @FXML private ComboBox<Apartamento> cbApartamento;
    @FXML private TextField txtDescripcion;
    @FXML private TableView<Novedad> tblNovedades;
    @FXML private Button btnCerrar;

    @FXML
    public void initialize() {
        Tablas.mostrarNombres(cbApartamento);
        cbApartamento.setItems(FXCollections.observableArrayList(controller.listarApartamentos()));
        Tablas.columna(tblNovedades, "Fecha", n -> n.getFecha().toLocalDate() + " " + n.getFecha().toLocalTime().withNano(0));
        Tablas.columna(tblNovedades, "Apartamento", n -> n.getApartamento().getNombre());
        Tablas.columna(tblNovedades, "Descripción", Novedad::getDescripcion);
        Tablas.columna(tblNovedades, "Reportó", n -> n.getReportado().getNombre());
        recargar();
    }

    private void recargar() {
        tblNovedades.setItems(FXCollections.observableArrayList(controller.listarNovedades()));
    }

    @FXML
    void onRegistrar(ActionEvent event) {
        try {
            controller.registrar(cbApartamento.getValue(), txtDescripcion.getText());
            txtDescripcion.clear();
            recargar();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCerrar(ActionEvent event) {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
}
