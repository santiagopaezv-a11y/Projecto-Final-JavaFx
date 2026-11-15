package Aparta_Suites_UQ.viewController.admin;
import Aparta_Suites_UQ.controller.Informe;
import Aparta_Suites_UQ.viewController.util.Tablas;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import Aparta_Suites_UQ.controller.admin.InterfazInformeController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazInformeViewController {
    private final InterfazInformeController controller = new InterfazInformeController();
    @FXML
    private Button buttom_apartamento;

    @FXML
    private Button buttom_configuracion;

    @FXML
    private Button buttom_informe;

    @FXML
    private Button buttom_empleado;
    @FXML
    private Button buttom_bloqueo;

    @FXML
    private Button buttom_inicio;

    @FXML
    private Button buttom_temporada;
    @FXML
    private Rectangle rect_bloqueo_buttom;
    @FXML
    private Rectangle rect_informe_buttom;

    @FXML
    private Rectangle rect_apart_buttom;

    @FXML
    private Rectangle rect_config_buttom;

    @FXML
    private Rectangle rect_empleado_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_temporada_buttom;
    @FXML
    void vistaMouseEncimaInformes(MouseEvent event) {
        rect_informe_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraInformes(MouseEvent event) {
        rect_informe_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaEmpleados(MouseEvent event) {
        rect_empleado_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraEmpleados(MouseEvent event) {
        rect_empleado_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaApart(MouseEvent event) {
        rect_apart_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraApart(MouseEvent event) {
        rect_apart_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaConfig(MouseEvent event) {
        rect_config_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraConfig(MouseEvent event) {
        rect_config_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaTemporada(MouseEvent event) {
        rect_temporada_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraTemporada(MouseEvent event) {
        rect_temporada_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseEncimaBloqueo(MouseEvent event) {rect_bloqueo_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraBloqueo(MouseEvent event) {
        rect_bloqueo_buttom.setVisible(false);
    }
    @FXML
    private void mostrarPanelApartamento(ActionEvent event){
        Vista.cambiarEscena(event, Paths.APARTAMENTO_PANEL);
    }
    @FXML
    private void mostrarPanelTemporadas(ActionEvent event){
        Vista.cambiarEscena(event, Paths.TEMPORADAS_PANEL);
    }
    @FXML
    private void mostrarPanelEmpleados(ActionEvent event){
        Vista.cambiarEscena(event, Paths.EMPLEADO_PANEL);
    }
    @FXML
    private void mostrarPanelInforme(){
    }
    @FXML
    private void mostrarPanelConfiguracion(ActionEvent event){
        Vista.cambiarEscena(event,Paths.CONFIGURACION_PANEL);
    }
    @FXML
    private void mostrarPanelBloqueo(ActionEvent event){
        Vista.cambiarEscena(event,Paths.BLOQUEO_PANEL);
    }
    @FXML
    private void mostrarPanelInicio(ActionEvent event){
        Vista.cambiarEscena(event,Paths.ADMINPANEL);
    }

    @FXML private ComboBox<String> cbInforme;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private Label lblTituloInforme;
    @FXML private TableView<List<String>> tblInforme;

    @FXML
    public void initialize() {
        cbInforme.setItems(FXCollections.observableArrayList(controller.nombresDeInformes()));
        dpDesde.setValue(LocalDate.now().withDayOfMonth(1));
        dpHasta.setValue(LocalDate.now());
        cbInforme.valueProperty().addListener((obs, anterior, nombre) -> {
            boolean usaPeriodo = nombre != null && controller.requierePeriodo(nombre);
            dpDesde.setDisable(!usaPeriodo);
            dpHasta.setDisable(!usaPeriodo);
        });
    }

    @FXML
    void onGenerarInforme(ActionEvent event) {
        try {
            Informe informe = controller.generar(cbInforme.getValue(), dpDesde.getValue(), dpHasta.getValue());
            lblTituloInforme.setText(informe.titulo());
            Tablas.cargarInforme(tblInforme, informe);
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }
}
