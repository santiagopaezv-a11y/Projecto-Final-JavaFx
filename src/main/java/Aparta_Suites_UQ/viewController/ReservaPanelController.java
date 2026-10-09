package Aparta_Suites_UQ.viewController;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class ReservaPanelController {
    @FXML
    private Button buttom_apartamento;

    @FXML
    private Button buttom_configuracion;

    @FXML
    private Button buttom_folio;

    @FXML
    private Button buttom_huesped;

    @FXML
    private Button buttom_inicio;

    @FXML
    private Button buttom_reserva;
    @FXML
    private Rectangle rect_folio_buttom;

    @FXML
    private Rectangle rect_apart_buttom;

    @FXML
    private Rectangle rect_config_buttom;

    @FXML
    private Rectangle rect_huesped_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;
    @FXML
    void vistaMouseEncimaFolio(MouseEvent event) {
        rect_folio_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraFolio(MouseEvent event) {
        rect_folio_buttom.setVisible(false);
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
    void vistaMouseEncimaHuesped(MouseEvent event) {
        rect_huesped_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraHuesped(MouseEvent event) {
        rect_huesped_buttom.setVisible(false);
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
    void vistaMouseEncimaReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(false);
    }
    @FXML
    void seleccionarPanelApartamento(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.APARTAMENTO_PANEL);
    }
    @FXML
    void seleccionarPanelConfiguracion(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.CONFIGURACION_PANEL);
    }
    @FXML
    void seleccionarPanelFolio(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.FOLIO_PANEL);
    }
    @FXML
    void seleccionarPanelHuesped(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.HUESPED_PANEL);
    }
    @FXML
    void seleccionarPanelDashboard(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.DASHBOARD);
    }
}
