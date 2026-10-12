package Aparta_Suites_UQ.viewController.admin;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ApartamentoPanelController implements Initializable {
    @FXML
    private ScrollPane scrollPanePrincipal;
    @FXML
    private FlowPane contenedorTarjetas;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (scrollPanePrincipal != null) {
            contenedorTarjetas.prefWidthProperty().bind(scrollPanePrincipal.widthProperty().subtract(30));
        }
        cargarTarjetasApartamentos();
    }
    private void cargarTarjetasApartamentos() {
        List<Apartamento> listaDeApartamentos = /*Navegacion.getInstance().getListaApartamentos();*/obtenerApartamentosDePrueba();
        contenedorTarjetas.getChildren().clear();
        for (Apartamento apt : listaDeApartamentos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.APARTAMENTO_CARD));
                Node tarjetaNode = loader.load();
                ApartamentoCard cardController = loader.getController();
                cardController.setApartamento(apt);
                cardController.setOnEliminarListener(() -> {
                    cargarTarjetasApartamentos();
                });
                contenedorTarjetas.getChildren().add(tarjetaNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Base de datos de prueba
    private List<Apartamento> obtenerApartamentosDePrueba() {
        return List.of(
                new Apartamento("ApartamentoPedro", 1, 2, "Balcon",150000,EstadoOperativo.EN_PREPARACION),
                new Apartamento("Sanchez", 2, 4, "Balcon y Cocina",180000,EstadoOperativo.PREPARADO),
                new Apartamento("Paez", 3, 5, "Piscina",200000,EstadoOperativo.FUERA_DE_SERVICIO),
                new Apartamento("Casallas", 2, 4, "Aire acondicionado",160000,EstadoOperativo.OCUPADO),
                new Apartamento("Samuel",3,6,"Balcon y aire acondicionado", 175000,EstadoOperativo.OCUPADO),
                new Apartamento("Apartamento2", 2, 4, "Balcon",150000,EstadoOperativo.EN_PREPARACION),
                new Apartamento("Sanchez3", 2, 4, "Balcon y Cocina",180000,EstadoOperativo.PENDIENTE),
                new Apartamento("Paesihno", 5, 12, "Piscina",200000,EstadoOperativo.FUERA_DE_SERVICIO),
                new Apartamento("Casañas", 1, 1, "Aire acondicionado",160000,EstadoOperativo.OCUPADO),
                new Apartamento("Maria",3,6,"Balcon y aire acondicionado", 175000,EstadoOperativo.PENDIENTE)

        );
    }
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
        cargarTarjetasApartamentos();
    }
}
