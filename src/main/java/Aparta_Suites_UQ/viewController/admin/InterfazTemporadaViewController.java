package Aparta_Suites_UQ.viewController.admin;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.alojamiento.TipoTemporada;
import Aparta_Suites_UQ.viewController.util.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.admin.InterfazTemporadaController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazTemporadaViewController {
    private final InterfazTemporadaController controller = new InterfazTemporadaController();
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
    private void mostrarPanelApartamento(ActionEvent event){
        Vista.cambiarEscena(event, Paths.APARTAMENTO_PANEL);
    }
    @FXML
    private void mostrarPanelTemporadas(){
    }
    @FXML
    private void mostrarPanelEmpleados(ActionEvent event){
        Vista.cambiarEscena(event, Paths.EMPLEADO_PANEL);
    }
    @FXML
    private void mostrarPanelInforme(ActionEvent event){
        Vista.cambiarEscena(event, Paths.INFORME_PANEL);
    }
    @FXML
    private void mostrarPanelConfiguracion(ActionEvent event){
        Vista.cambiarEscena(event,Paths.CONFIGURACION_PANEL);
    }
    @FXML
    private void mostrarPanelBloqueo(ActionEvent event){
        Vista.cambiarEscena(event,Paths.BLOQUEO_PANEL);
    }
    @FXML
    private void mostrarPanelInicio(ActionEvent event){
        Vista.cambiarEscena(event,Paths.ADMINPANEL);
    }

    @FXML private ComboBox<TipoTemporada> cbTipo;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private TableView<Temporada> tblTemporadas;
    @FXML private ComboBox<Apartamento> cbApartamento;
    @FXML private TextField txtBaja;
    @FXML private TextField txtMedia;
    @FXML private TextField txtAlta;
    @FXML private TableView<Apartamento> tblTarifas;

    @FXML
    public void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList(TipoTemporada.MEDIA, TipoTemporada.ALTA));
        Tablas.mostrarNombres(cbApartamento);

        Tablas.columna(tblTemporadas, "Tipo", t -> t.getTipo().name());
        Tablas.columna(tblTemporadas, "Inicio", t -> t.getFechaInicio().toString());
        Tablas.columna(tblTemporadas, "Fin", t -> t.getFechaFin().toString());
        tblTemporadas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, t) -> {
            if (t != null) {
                cbTipo.setValue(t.getTipo());
                dpInicio.setValue(t.getFechaInicio());
                dpFin.setValue(t.getFechaFin());
            }
        });

        Tablas.columna(tblTarifas, "Apartamento", Apartamento::getNombre);
        Tablas.columna(tblTarifas, "Baja", a -> Tablas.dinero(a.getTarifaSegunTemporada(TipoTemporada.BAJA)));
        Tablas.columna(tblTarifas, "Media", a -> Tablas.dinero(a.getTarifaSegunTemporada(TipoTemporada.MEDIA)));
        Tablas.columna(tblTarifas, "Alta", a -> Tablas.dinero(a.getTarifaSegunTemporada(TipoTemporada.ALTA)));
        tblTarifas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, a) -> {
            if (a != null) {
                cbApartamento.setValue(a);
                cargarTarifas(a);
            }
        });
        recargar();
    }

    private void recargar() {
        tblTemporadas.setItems(FXCollections.observableArrayList(controller.listarTemporadas()));
        tblTarifas.setItems(FXCollections.observableArrayList(controller.listarApartamentos()));
        cbApartamento.setItems(FXCollections.observableArrayList(controller.listarApartamentos()));
        tblTemporadas.refresh();
        tblTarifas.refresh();
    }

    private void cargarTarifas(Apartamento apartamento) {
        txtBaja.setText(String.valueOf(apartamento.getTarifaSegunTemporada(TipoTemporada.BAJA)));
        txtMedia.setText(String.valueOf(apartamento.getTarifaSegunTemporada(TipoTemporada.MEDIA)));
        txtAlta.setText(String.valueOf(apartamento.getTarifaSegunTemporada(TipoTemporada.ALTA)));
    }

    @FXML
    void onSeleccionarApartamento(ActionEvent event) {
        Apartamento apartamento = cbApartamento.getValue();
        if (apartamento != null) {
            cargarTarifas(apartamento);
        }
    }

    @FXML
    void onAgregarTemporada(ActionEvent event) {
        try {
            controller.crearTemporada(cbTipo.getValue(), dpInicio.getValue(), dpFin.getValue());
            limpiarTemporada();
            recargar();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onActualizarTemporada(ActionEvent event) {
        try {
            controller.actualizarTemporada(tblTemporadas.getSelectionModel().getSelectedItem(),
                    cbTipo.getValue(), dpInicio.getValue(), dpFin.getValue());
            recargar();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onEliminarTemporada(ActionEvent event) {
        Temporada seleccionada = tblTemporadas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Vista.enviarAlertaWarning("Seleccione una temporada de la tabla.");
            return;
        }
        if (Vista.enviarAlertaConfirmacion("¿Eliminar la temporada " + seleccionada.getTipo() + " del "
                + seleccionada.getFechaInicio() + " al " + seleccionada.getFechaFin() + "?")) {
            controller.eliminarTemporada(seleccionada);
            limpiarTemporada();
            recargar();
        }
    }

    @FXML
    void onGuardarTarifas(ActionEvent event) {
        try {
            controller.guardarTarifas(cbApartamento.getValue(), txtBaja.getText(), txtMedia.getText(), txtAlta.getText());
            tblTarifas.refresh();
            Vista.enviarAlertaInfo("Tarifas guardadas. Se aplican a las reservas que se creen desde ahora.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    private void limpiarTemporada() {
        cbTipo.setValue(null);
        dpInicio.setValue(null);
        dpFin.setValue(null);
        tblTemporadas.getSelectionModel().clearSelection();
    }
}
