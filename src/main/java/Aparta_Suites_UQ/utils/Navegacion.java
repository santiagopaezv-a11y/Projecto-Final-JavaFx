package Aparta_Suites_UQ.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Navegacion {
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
}
