package Aparta_Suites_UQ.utils;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.Reserva;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Navegacion {
    private static Navegacion instance;
    private final List<Apartamento> listaApartamentos = new ArrayList<>();
    private final List<Reserva> listaReservas=new ArrayList<>();
    public static void cambiarEscena(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(rutaFxml));
            Parent root = loader.load();
            Node source = (Node) event.getSource();
            Stage stagePrincipal = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stagePrincipal.setScene(scene);
            stagePrincipal.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static <T> T abrirModal(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(rutaFxml));
            Parent root = loader.load();
            Node source = (Node) event.getSource();
            Stage stagePadre = (Stage) source.getScene().getWindow();
            Stage stageModal = new Stage();
            stageModal.initOwner(stagePadre);
            stageModal.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stageModal.setScene(scene);
            stageModal.showAndWait();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void enviarAlertaWarning(String mensaje){
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public static void enviarAlertaInfo(String mensaje){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public static void enviarAlertaConfirmacion(String mensaje){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static boolean validarCredencial(TextField textUser,TextField textContraseña,String usuario, String contrasena){
        if (textUser==null||textContraseña==null) return false;
        String userIngresado=textUser.getText().trim();
        String contrasenaIngresada=textContraseña.getText().trim();
        return usuario.equals(userIngresado)&&contrasena.equals(contrasenaIngresada);
    }
    public static Navegacion getInstance() {
        if (instance == null) {
            instance = new Navegacion();
        }
        return instance;
    }
    public List<Apartamento> getListaApartamentos() {
        return listaApartamentos;
    }
    public List<Reserva> getListaReservas() {
        return listaReservas;
    }
    private Apartamento apartamentoSeleccionadoParaEditar;

    public Apartamento getApartamentoSeleccionadoParaEditar() {
        return apartamentoSeleccionadoParaEditar;
    }

    public void setApartamentoSeleccionadoParaEditar(Apartamento apartamento) {
        this.apartamentoSeleccionadoParaEditar = apartamento;
    }
}
