package Aparta_Suites_UQ.viewController;

import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazCotizacionController {
    @FXML
    private Button buttom_cotizacion;

    @FXML
    private Button buttom_pago;

    @FXML
    private Button buttom_inicio;

    @FXML
    private Button buttom_reserva;
    @FXML
    private Rectangle rect_cotizacion_buttom;

    @FXML
    private Rectangle rect_pago_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;
    @FXML
    void vistaMouseEncimaPago(MouseEvent event) {
        rect_pago_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraPago(MouseEvent event) {
        rect_pago_buttom.setVisible(false);
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
    void vistaMouseEncimaCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(true);
    }
    @FXML
    private void vistaMouseAfueraCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(false);
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
    void mostrarPanelPagos(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.PAGO_PANEL);
    }
    @FXML
    void mostrarPanelCotizacion(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.COTIZACION_PANEL);
    }
    @FXML
    void mostrarPanelReserva(ActionEvent event){
        Navegacion.cambiarEscena(event, Paths.RESERVA_PANEL);
    }
    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event){
        Navegacion.cambiarEscena(event,Paths.USUARIO_PANEL);
    }
}
