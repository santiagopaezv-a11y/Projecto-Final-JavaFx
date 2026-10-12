package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;

public class InterfazUsuarioController {
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
        Navegacion.cambiarEscena(event, Paths.COTIZACION_PANEL);
    }

    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event) {

    }

    @FXML
    void mostrarPanelPagos(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.PAGO_PANEL);
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.RESERVA_PANEL);
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
        String nombreUsuarioRecepcion="recepcion123";
        String contrasenaRecepcion="123";
        String nombreUsuarioAdmin="admin123";
        String contrasenaAdmin="1234";
        if (Navegacion.validarCredencial(txtNombreUsuario,txtContrasenaUsuario,nombreUsuarioAdmin,contrasenaAdmin)){
            Navegacion.cambiarEscena(event,Paths.ADMINPANEL);
            return;
        }else if(Navegacion.validarCredencial(txtNombreUsuario,txtContrasenaUsuario,nombreUsuarioRecepcion,contrasenaRecepcion)){
            Navegacion.cambiarEscena(event,Paths.RECEPCION_PANEL);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia del Sistema");
            alert.setHeaderText("El usuario o contrasena esta incorrecto");
            alert.showAndWait();
        }
        panelChangeUsuario.setVisible(false);
    }
}
