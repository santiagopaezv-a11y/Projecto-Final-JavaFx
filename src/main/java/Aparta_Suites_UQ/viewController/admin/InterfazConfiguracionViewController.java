package Aparta_Suites_UQ.viewController.admin;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.admin.InterfazConfiguracionController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazConfiguracionViewController {
    private final InterfazConfiguracionController controller = new InterfazConfiguracionController();
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
    }
    @FXML
    private void mostrarPanelBloqueo(ActionEvent event){
        Vista.cambiarEscena(event,Paths.BLOQUEO_PANEL);
    }
    @FXML
    private void mostrarPanelInicio(ActionEvent event){
        Vista.cambiarEscena(event,Paths.ADMINPANEL);
    }

    @FXML private Label lblPolitica;
    @FXML private TextField txtDiasTotal;
    @FXML private TextField txtDiasParcial;
    @FXML private TextField txtPorcentaje;
    @FXML private TextField txtNombre;
    @FXML private TextField txtNit;
    @FXML private TextField txtDireccion;

    @FXML
    public void initialize() {
        cargarPolitica();
        txtNombre.setText(controller.getNombre());
        txtNit.setText(controller.getNit());
        txtDireccion.setText(controller.getDireccion());
    }

    private void cargarPolitica() {
        txtDiasTotal.setText(String.valueOf(controller.getPolitica().diasDevolucionTotal()));
        txtDiasParcial.setText(String.valueOf(controller.getPolitica().diasRetencionParcial()));
        txtPorcentaje.setText(String.valueOf(Math.round(controller.getPolitica().porcentajeRetencionParcial() * 100)));
        lblPolitica.setText("Vigente: " + controller.getPolitica().descripcion());
    }

    @FXML
    void onGuardarPolitica(ActionEvent event) {
        try {
            controller.guardarPolitica(txtDiasTotal.getText(), txtDiasParcial.getText(), txtPorcentaje.getText());
            cargarPolitica();
            Vista.enviarAlertaInfo("Política de cancelación guardada. Se aplicará a las reservas nuevas.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onGuardarDatos(ActionEvent event) {
        try {
            controller.guardarDatos(txtNombre.getText(), txtNit.getText(), txtDireccion.getText());
            Vista.enviarAlertaInfo("Datos del alojamiento guardados.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }
}
