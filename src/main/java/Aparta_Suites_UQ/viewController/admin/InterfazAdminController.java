package Aparta_Suites_UQ.viewController.admin;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

import javafx.event.ActionEvent;

public class InterfazAdminController {
    @FXML
    private Button buttom_guestUser;
    @FXML
    private Button buttom_accesRecepcion;
    @FXML
    private Button buttom_changeUsuario;
    @FXML
    private AnchorPane panelChangeUsuario;
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
    private TextField txtContrasenaUsuario;

    @FXML
    private TextField txtNombreUsuario;
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
    void vistaMouseAfueraInicio(MouseEvent event) {
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
        Navegacion.cambiarEscena(event, Paths.APARTAMENTO_PANEL);
    }
    @FXML
    private void mostrarPanelTemporadas(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.TEMPORADAS_PANEL);
    }
    @FXML
    private void mostrarPanelEmpleados(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.EMPLEADO_PANEL);
    }
    @FXML
    private void mostrarPanelInforme(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.INFORME_PANEL);
    }
    @FXML
    private void mostrarPanelConfiguracion(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.CONFIGURACION_PANEL);
    }
    @FXML
    private void mostrarPanelBloqueo(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.BLOQUEO_PANEL);
    }
    @FXML
    private void mostrarPanelInicio(){
    }
    @FXML
    private void mostrarPanelGestionUser() {
        panelChangeUsuario.setVisible(true);
    }
    @FXML
    private void ocultarPanelAlClickearFuera(MouseEvent event) {
        if (panelChangeUsuario.isVisible() && !panelChangeUsuario.getBoundsInParent().contains(event.getX(), event.getY())) {
            panelChangeUsuario.setVisible(false);
        }
    }
    @FXML
    private void mostrarPanelInicioRecepcion(ActionEvent event){
    }
    @FXML
    private void cambiarModuloRecepcion(ActionEvent event){
        String nombreUsuarioRecepcion="recepcion123";
        String contrasenaRecepcion="123";
        if (Navegacion.validarCredencial(txtNombreUsuario,txtContrasenaUsuario,nombreUsuarioRecepcion,contrasenaRecepcion)){
            Navegacion.cambiarEscena(event,Paths.RECEPCION_PANEL);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia del Sistema");
            alert.setHeaderText("El usuario o contrasena esta incorrecto");
            alert.showAndWait();
        }
    }
    @FXML
    private void cambiarModuloUser(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.USUARIO_PANEL);
    }
}
