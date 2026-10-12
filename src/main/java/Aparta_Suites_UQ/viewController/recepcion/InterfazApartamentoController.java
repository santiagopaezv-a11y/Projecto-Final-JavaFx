package Aparta_Suites_UQ.viewController.recepcion;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazApartamentoController {
    @FXML
    private Button buttom_Check;

    @FXML
    private Button buttom_apartamento;

    @FXML
    private Button buttom_folio;

    @FXML
    private Button buttom_inicioRecepcion;

    @FXML
    private Button buttom_reservaDirecta;

    @FXML
    private Rectangle rect_apartamento_buttom;

    @FXML
    private Rectangle rect_check_buttom;

    @FXML
    private Rectangle rect_folio_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;

    @FXML
    void mostrarPanelCheck(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.CHECK_PANEL);
    }

    @FXML
    void mostrarPanelFolio(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.FOLIO_PANEL);
    }

    @FXML
    void mostrarPanelApartamento() {
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.RESERVADIRECTA_PANEL);
    }

    @FXML
    void vistaMouseAfueraFolio(MouseEvent event) {
        rect_folio_buttom.setVisible(false);
    }
    @FXML
    void vistaMouseAfueraApartamento(MouseEvent event) {
        rect_apartamento_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraCheck(MouseEvent event) {
        rect_check_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseEncimaFolio(MouseEvent event) {
        rect_folio_buttom.setVisible(true);
    }
    @FXML
    void vistaMouseEncimaApartamento(MouseEvent event) {
        rect_apartamento_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaCheck(MouseEvent event) {
        rect_check_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(true);
    }
    @FXML
    private void mostrarPanelInicioRecepcion(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.RECEPCION_PANEL);
    }

}
