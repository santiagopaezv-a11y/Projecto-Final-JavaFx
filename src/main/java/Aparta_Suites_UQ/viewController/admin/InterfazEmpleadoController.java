package Aparta_Suites_UQ.viewController.admin;

import Aparta_Suites_UQ.model.huesped.Empleado;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class InterfazEmpleadoController {

    // --- Componentes Menú Lateral ---
    @FXML private Button buttom_apartamento;
    @FXML private Button buttom_configuracion;
    @FXML private Button buttom_informe;
    @FXML private Button buttom_empleado;
    @FXML private Button buttom_bloqueo;
    @FXML private Button buttom_inicio;
    @FXML private Button buttom_temporada;

    @FXML private Rectangle rect_bloqueo_buttom;
    @FXML private Rectangle rect_informe_buttom;
    @FXML private Rectangle rect_apart_buttom;
    @FXML private Rectangle rect_config_buttom;
    @FXML private Rectangle rect_empleado_buttom;
    @FXML private Rectangle rect_inicio_buttom;
    @FXML private Rectangle rect_temporada_buttom;

    // --- Componentes Gestión Empleados ---
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltroRol;
    @FXML private Button btnLimpiarFiltros;
    @FXML private Button btnNuevoEmpleado;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, String> colDocumento;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colCorreo;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colFechaNacimiento;
    @FXML private TableColumn<Empleado, String> colUsuario;

    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
    private FilteredList<Empleado> listaFiltrada;

    @FXML
    public void initialize() {
        rect_empleado_buttom.setVisible(true);

        cargarComboRol();
        configurarTabla();
        cargarDatosEmpleados();
        configurarBuscadorEnTiempoReal();
    }

    private void cargarComboRol() {
        cbFiltroRol.setItems(FXCollections.observableArrayList("TODOS", "ADMINISTRADOR", "RECEPCIONISTA"));
        cbFiltroRol.setValue("TODOS");
        cbFiltroRol.setOnAction(e -> aplicarFiltros());
    }

    private void configurarTabla() {
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        // Uso directo de la propiedad 'rol' a través del getter getRol()
        colRol.setCellValueFactory(cellData -> {
            Object rolObj = cellData.getValue().getRol();
            return new SimpleStringProperty(rolObj != null ? rolObj.toString() : "");
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colFechaNacimiento.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaNacimiento() != null) {
                return new SimpleStringProperty(cellData.getValue().getFechaNacimiento().format(formatter));
            }
            return new SimpleStringProperty("");
        });
    }

    private void cargarDatosEmpleados() {
        List<Empleado> guardados = Navegacion.getInstance().getListaEmpleados();
        listaEmpleados.setAll(guardados);

        listaFiltrada = new FilteredList<>(listaEmpleados, p -> true);
        tblEmpleados.setItems(listaFiltrada);
    }

    private void configurarBuscadorEnTiempoReal() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        String textoBusqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";
        String rolSeleccionado = cbFiltroRol.getValue();

        listaFiltrada.setPredicate(empleado -> {
            boolean coincideTexto = textoBusqueda.isEmpty() ||
                    (empleado.getNombre() != null && empleado.getNombre().toLowerCase().contains(textoBusqueda)) ||
                    (empleado.getDocumento() != null && empleado.getDocumento().toLowerCase().contains(textoBusqueda)) ||
                    (empleado.getCorreo() != null && empleado.getCorreo().toLowerCase().contains(textoBusqueda));

            String rolStr = empleado.getRol() != null ? empleado.getRol().toString() : "";
            boolean coincideRol = rolSeleccionado == null || rolSeleccionado.equals("TODOS") ||
                    rolStr.equalsIgnoreCase(rolSeleccionado);

            return coincideTexto && coincideRol;
        });
    }

    @FXML
    void onLimpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        cbFiltroRol.setValue("TODOS");
        listaFiltrada.setPredicate(p -> true);
    }

    @FXML
    void onNuevoEmpleado(ActionEvent event) {
        Navegacion.getInstance().setEmpleadoEditar(null);
        Navegacion.abrirModal(event, Paths.ADD_EMPLEADO);
        cargarDatosEmpleados();
    }

    @FXML
    void onEditarEmpleado(ActionEvent event) {
        Empleado seleccionado = tblEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Navegacion.enviarAlertaWarning("Por favor seleccione un empleado de la tabla.");
            return;
        }
        Navegacion.getInstance().setEmpleadoEditar(seleccionado);
        Navegacion.abrirModal(event, Paths.ADD_EMPLEADO);
        cargarDatosEmpleados();
    }

    @FXML
    void onEliminarEmpleado(ActionEvent event) {
        Empleado seleccionado = tblEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Navegacion.enviarAlertaWarning("Seleccione el empleado que desea eliminar.");
            return;
        }

        boolean confirmar = Navegacion.enviarAlertaConfirmacion(
                "¿Está seguro de que desea eliminar a " + seleccionado.getNombre());
        if (confirmar) {
            Navegacion.getInstance().getListaEmpleados().remove(seleccionado);
            cargarDatosEmpleados();
            Navegacion.enviarAlertaInfo("Empleado eliminado correctamente.");
        }
    }

    // --- Controladores de Eventos del Menú Lateral ---
    @FXML void vistaMouseEncimaInformes(MouseEvent event) { rect_informe_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraInformes(MouseEvent event) { rect_informe_buttom.setVisible(false); }
    @FXML void vistaMouseEncimaInicio(MouseEvent event) { rect_inicio_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraInicio(MouseEvent event) { rect_inicio_buttom.setVisible(false); }
    @FXML void vistaMouseEncimaEmpleados(MouseEvent event) { rect_empleado_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraEmpleados(MouseEvent event) { }
    @FXML void vistaMouseEncimaApart(MouseEvent event) { rect_apart_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraApart(MouseEvent event) { rect_apart_buttom.setVisible(false); }
    @FXML void vistaMouseEncimaConfig(MouseEvent event) { rect_config_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraConfig(MouseEvent event) { rect_config_buttom.setVisible(false); }
    @FXML void vistaMouseEncimaTemporada(MouseEvent event) { rect_temporada_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraTemporada(MouseEvent event) { rect_temporada_buttom.setVisible(false); }
    @FXML void vistaMouseEncimaBloqueo(MouseEvent event) { rect_bloqueo_buttom.setVisible(true); }
    @FXML private void vistaMouseAfueraBloqueo(MouseEvent event) { rect_bloqueo_buttom.setVisible(false); }

    @FXML private void mostrarPanelApartamento(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.APARTAMENTO_PANEL); }
    @FXML private void mostrarPanelTemporadas(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.TEMPORADAS_PANEL); }
    @FXML private void mostrarPanelEmpleados() { }
    @FXML private void mostrarPanelInforme(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.INFORME_PANEL); }
    @FXML private void mostrarPanelConfiguracion(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.CONFIGURACION_PANEL); }
    @FXML private void mostrarPanelBloqueo(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.BLOQUEO_PANEL); }
    @FXML private void mostrarPanelInicio(ActionEvent event) { Navegacion.cambiarEscena(event, Paths.ADMINPANEL); }
}