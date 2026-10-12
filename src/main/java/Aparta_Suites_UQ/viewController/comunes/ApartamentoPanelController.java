package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import javax.swing.*;

public class ApartamentoPanelController {
    @FXML
    private Button buttom_addApart;
    @FXML
    private Button buttom_apartamento;
    @FXML
    private Button buttom_bloqueo;

    @FXML
    private Button buttom_configuracion;

    @FXML
    private Button buttom_informe;

    @FXML
    private Button buttom_empleado;

    @FXML
    private Button buttom_inicio;

    @FXML
    private Button buttom_temporada;
    @FXML
    private Rectangle rect_informe_buttom;
    @FXML
    private Rectangle rect_bloqueo_buttom;
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
    void vistaMouseEncimaTemporadas(MouseEvent event) {
        rect_temporada_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraTemporadas(MouseEvent event) {
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
    private void mostrarPanelApartamento(){
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
    private void mostrarPanelInicio(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.ADMINPANEL);
    }
    @FXML
    private void pedirDatosApartamento(ActionEvent event){
        Navegacion.abrirModal(event,Paths.ADDAPARTAMENTO_PANEL);
    }
}
