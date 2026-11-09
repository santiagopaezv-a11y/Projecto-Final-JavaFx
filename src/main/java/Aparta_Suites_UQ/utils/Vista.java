package Aparta_Suites_UQ.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
public final class Vista {

    private Vista() {
    }

    public static void cambiarEscena(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Vista.class.getResource(rutaFxml));
            Parent root = loader.load();
            Node source = (Node) event.getSource();
            Stage stagePrincipal = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stagePrincipal.setScene(scene);
            stagePrincipal.setTitle("Aparta Suites UQ");
            stagePrincipal.centerOnScreen();
            stagePrincipal.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T abrirModal(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Vista.class.getResource(rutaFxml));
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

    public static void enviarAlertaWarning(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void enviarAlertaInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static boolean enviarAlertaConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void enviarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
