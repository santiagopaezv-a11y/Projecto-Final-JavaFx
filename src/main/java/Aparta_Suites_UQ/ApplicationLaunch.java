package Aparta_Suites_UQ;

import Aparta_Suites_UQ.controller.servicio.DatosPruebaServicio;
import Aparta_Suites_UQ.utils.PersistenciaUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationLaunch extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Empresa empresaCargada = PersistenciaUtil.cargarEmpresa();
        if (empresaCargada != null) {
            Empresa.setInstance(empresaCargada);
            Empresa.getInstance().sincronizarContadores();
            System.out.println("[App]  Datos cargados e instalados en Empresa.getInstance()");
        } else {
            System.out.println("[App] Iniciando con una nueva instancia de Empresa.");
        }
        //Datos de prueba si se desea revisar:
        //DatosPruebaServicio.cargar();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/comunes/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("SGA - Aparta-Suites UQ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public void stop() throws Exception {
        PersistenciaUtil.guardarEmpresa(Empresa.getInstance());
        super.stop();
    }
    public static void main(String[] args) {
        launch(args);
    }
}