package Aparta_Suites_UQ.viewController.recepcion;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import Aparta_Suites_UQ.viewController.comunes.ApartamentoCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InterfazApartamentoController implements Initializable{
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
    private FlowPane contenedorTarjetas;

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
    private ScrollPane scrollPanePrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Ajustar el ancho del FlowPane respecto al ScrollPane
        if (scrollPanePrincipal != null && contenedorTarjetas != null) {
            contenedorTarjetas.prefWidthProperty().bind(scrollPanePrincipal.widthProperty().subtract(30));
        }

        // 2. Cargar las tarjetas al abrir la pantalla
        cargarTarjetasApartamentos();
    }

    private void cargarTarjetasApartamentos() {
        List<Apartamento> listaDeApartamentos = Navegacion.getInstance().getListaApartamentos();
        contenedorTarjetas.getChildren().clear();

        for (Apartamento apt : listaDeApartamentos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.APARTAMENTO_CARD));
                Node tarjetaNode = loader.load();

                ApartamentoCard cardController = loader.getController();
                cardController.setApartamento(apt);

                // Activar el modo solo estado para esta interfaz
                cardController.setModoSoloEstado(true);

                // Listeners de actualización y eliminación
                cardController.setOnEliminarListener(() -> cargarTarjetasApartamentos());
                cardController.setOnActualizarListener(() -> cargarTarjetasApartamentos());

                contenedorTarjetas.getChildren().add(tarjetaNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
