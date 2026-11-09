package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.controller.user.InterfazUsuarioController;
import Aparta_Suites_UQ.controller.Acceso;
import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;

public class InterfazUsuarioViewController {
    private final InterfazUsuarioController controller = new InterfazUsuarioController();
    @FXML
    private Button buttom_changeUsuario;
    @FXML
    private Button buttom_accesModulo;
    @FXML
    private AnchorPane panelChangeUsuario;
    @FXML
    private Button buttom_Cotizacion;

    @FXML
    private Button buttom_Pagos;

    @FXML
    private Button buttom_inicioUsuario;

    @FXML
    private Button buttom_reserva;
    @FXML
    private TextField txtContrasenaUsuario;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private Rectangle rect_cotizacion_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_pagos_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;

    @FXML
    void mostrarPanelCotizacion(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.COTIZACION_PANEL);
    }

    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event) {

    }

    @FXML
    void mostrarPanelPagos(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.PAGO_PANEL);
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
    @FXML
    private void mostrarPanelGestionUsuario() {
        panelChangeUsuario.setVisible(true);
    }
    @FXML
    private void ocultarPanelAlClickearFuera(MouseEvent event) {
        if (panelChangeUsuario.isVisible() && !panelChangeUsuario.getBoundsInParent().contains(event.getX(), event.getY())) {
            panelChangeUsuario.setVisible(false);
        }
    }
    @FXML
    private void cambiarModulo(ActionEvent event){
        Acceso acceso = controller.autenticarModulo(txtNombreUsuario.getText(), txtContrasenaUsuario.getText());
        if (acceso == Acceso.ADMINISTRADOR){
            Vista.cambiarEscena(event,Paths.ADMINPANEL);
            return;
        }else if(acceso == Acceso.RECEPCION){
            Vista.cambiarEscena(event,Paths.RECEPCION_PANEL);
        }else{
            Vista.enviarAlerta(Alert.AlertType.WARNING, "Advertencia del Sistema", "El usuario o contrasena esta incorrecto", null);
        }
        panelChangeUsuario.setVisible(false);
    }
}
