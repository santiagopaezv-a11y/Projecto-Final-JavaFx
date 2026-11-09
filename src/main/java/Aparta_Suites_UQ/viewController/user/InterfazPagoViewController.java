package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.controller.user.InterfazPagoController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazPagoViewController {
    private final InterfazPagoController controller = new InterfazPagoController();
    @FXML
    private Button buttom_Cotizacion;

    @FXML
    private Button buttom_Pagos;

    @FXML
    private Button buttom_inicioUsuario;

    @FXML
    private Button buttom_reserva;

    @FXML
    private Rectangle rect_cotizacion_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_pagos_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;

    @FXML
    void mostrarPanelContizacion(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.COTIZACION_PANEL);
    }

    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.USUARIO_PANEL);
    }

    @FXML
    void mostrarPanelPagos() {
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.RESERVA_PANEL);
    }

    @FXML
    void vistaMouseAfueraCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraPagos(MouseEvent event) {
        rect_pagos_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseEncimaCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaPagos(MouseEvent event) {
        rect_pagos_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(true);
    }
}
