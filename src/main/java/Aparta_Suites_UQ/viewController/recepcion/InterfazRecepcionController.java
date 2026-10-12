package Aparta_Suites_UQ.viewController.recepcion;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class InterfazRecepcionController {
    @FXML
    private Button buttom_Check;
    @FXML
    private Button buttom_accesModulo;
    @FXML
    private Button buttom_apartamento;

    @FXML
    private Button buttom_changeUsuario;

    @FXML
    private Button buttom_folio;

    @FXML
    private Button buttom_inicioRecepcion;

    @FXML
    private Button buttom_reservaDirecta;
    @FXML
    private TextField txtContraseñaUsuario;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private AnchorPane panelChangeUsuario;

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
    void mostrarPanelApartamento(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.APARTAMENTORECEPCION_PANEL);
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
    private void cambiarModuloUser(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.USUARIO_PANEL);
    }
    @FXML
    private void mostrarPanelInicioRecepcion(ActionEvent event){
    }
    @FXML
    private void cambiarModuloAdmin(ActionEvent event){
        String nombreUsuarioAdmin="admin123";
        String contrasenaAdmin="1234";
        if (Navegacion.validarCredencial(txtNombreUsuario,txtContraseñaUsuario,nombreUsuarioAdmin,contrasenaAdmin)){
            Navegacion.cambiarEscena(event,Paths.ADMINPANEL);
            return;
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia del Sistema");
            alert.setContentText("El usuario o contrasena esta incorrecto");
            alert.showAndWait();
        }
        panelChangeUsuario.setVisible(false);
    }

}
