package Aparta_Suites_UQ.utils;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Recepcionista;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Navegacion {
    private static Navegacion instance;
    private final List<Apartamento> listaApartamentos = new ArrayList<>();
    private final List<Reserva> listaReservas = new ArrayList<>();
    private final List<Huesped> listaHuespedes = new ArrayList<>();
    private final List<Temporada> listaTemporadas = new ArrayList<>();
    private final List<Empleado> listaEmpleados= new ArrayList<>();

    private Navegacion() {
        // Precargar temporadas por defecto al instanciar el Singleton
        cargarTemporadasPorDefecto();
    }

    /**
     * Inicializa las temporadas por defecto del alojamiento.
     */
    private void cargarTemporadasPorDefecto() {
        // 1. TEMPORADA ALTA (Diciembre - Enero)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 1, 31)
        ));

        // TEMPORADA ALTA (Semana Santa 2026)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 3, 29),
                LocalDate.of(2026, 4, 5)
        ));

        // TEMPORADA ALTA (Mitad de Año)
        listaTemporadas.add(new Temporada(
                TipoTemporada.ALTA,
                LocalDate.of(2026, 6, 15),
                LocalDate.of(2026, 7, 15)
        ));

        // 2. TEMPORADA MEDIA (Ejemplo: Puente Festivo)
        listaTemporadas.add(new Temporada(
                TipoTemporada.MEDIA,
                LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 5, 18)
        ));
    }

    public static void cambiarEscena(ActionEvent event, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(rutaFxml));
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

    public static boolean enviarAlertaConfirmacion(String mensaje){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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

    public List<Huesped> getListaHuespedes(){
        return listaHuespedes;
    }

    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void agregarTemporada(Temporada temporada) {
        if (temporada != null) {
            this.listaTemporadas.add(temporada);
        }
    }
    private Empleado recepcionistaEditar;
    private Huesped huespedEditar;
    private Apartamento apartamentoSeleccionadoParaEditar;
    private Reserva reservarEditar;

    public Empleado getEmpleadoEditar() {
        return recepcionistaEditar;
    }
    public void setEmpleadoEditar(Empleado recepcionistaEditar) {
        this.recepcionistaEditar = recepcionistaEditar;
    }
    public Huesped getHuespedEditar() {
        return huespedEditar;
    }

    public void setHuespedEditar(Huesped huespedEditar) {
        this.huespedEditar = huespedEditar;
    }

    public Reserva getReservarEditar() {
        return reservarEditar;
    }

    public void setReservarEditar(Reserva reservarEditar) {
        this.reservarEditar = reservarEditar;
    }

    public Apartamento getApartamentoSeleccionadoParaEditar() {
        return apartamentoSeleccionadoParaEditar;
    }

    public void setApartamentoSeleccionadoParaEditar(Apartamento apartamento) {
        this.apartamentoSeleccionadoParaEditar = apartamento;
    }
    public static boolean validarCredencialEmpleado(String user, String contrasena){
        boolean validar=false;
        for (Empleado empleado : getInstance().getListaEmpleados()){
            if (empleado.getUsuario().equals(user)&&empleado.getContrasena().equals(contrasena)){
                validar=true;
            }
        }
        return validar;
    }
}