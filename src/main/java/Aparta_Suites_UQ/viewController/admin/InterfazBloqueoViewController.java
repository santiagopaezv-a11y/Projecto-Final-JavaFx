package Aparta_Suites_UQ.viewController.admin;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.transacciones.Bloqueo;
import Aparta_Suites_UQ.viewController.util.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.admin.InterfazBloqueoController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazBloqueoViewController {
    private final InterfazBloqueoController controller = new InterfazBloqueoController();
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
    private void mostrarPanelInforme(ActionEvent event){
        Vista.cambiarEscena(event, Paths.INFORME_PANEL);
    }
    @FXML
    private void mostrarPanelConfiguracion(ActionEvent event){
        Vista.cambiarEscena(event, Paths.CONFIGURACION_PANEL);
    }
    @FXML
    private void mostrarPanelBloqueo(){
    }
    @FXML
    private void mostrarPanelInicio(ActionEvent event){
        Vista.cambiarEscena(event,Paths.ADMINPANEL);
    }
    @FXML private ComboBox<Apartamento> cbApartamento;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private TextField txtMotivo;
    @FXML private TableView<Bloqueo> tblBloqueos;
    @FXML
    public void initialize() {
        Tablas.mostrarNombres(cbApartamento);
        Tablas.columna(tblBloqueos, "Apartamento", b -> b.getApartamento().getNombre());
        Tablas.columna(tblBloqueos, "Desde", b -> b.getFechaInicio().toString());
        Tablas.columna(tblBloqueos, "Hasta (salida)", b -> b.getFechaFin().toString());
        Tablas.columna(tblBloqueos, "Motivo", Bloqueo::getMotivo);
        recargar();
    }
    private void recargar() {
        cbApartamento.setItems(FXCollections.observableArrayList(controller.listarApartamentos()));
        tblBloqueos.setItems(FXCollections.observableArrayList(controller.listarBloqueos()));
    }
    @FXML
    void onRegistrarBloqueo(ActionEvent event) {
        try {
            controller.crearBloqueo(cbApartamento.getValue(), dpInicio.getValue(), dpFin.getValue(), txtMotivo.getText());
            cbApartamento.setValue(null);
            dpInicio.setValue(null);
            dpFin.setValue(null);
            txtMotivo.clear();
            recargar();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }
    @FXML
    void onEliminarBloqueo(ActionEvent event) {
        Bloqueo seleccionado = tblBloqueos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Vista.enviarAlertaWarning("Seleccione un bloqueo de la tabla.");
            return;
        }
        if (Vista.enviarAlertaConfirmacion("¿Eliminar el bloqueo de " + seleccionado.getApartamento().getNombre() + "?")) {
            controller.eliminarBloqueo(seleccionado);
            recargar();
        }
    }
}
