package Aparta_Suites_UQ.viewController.recepcion;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.util.Callback;

import java.time.LocalDate;

public class InterfazReservaDirectaController {

    @FXML
    private TableColumn<Ocupante, String> colNombre;

    @FXML
    private TableColumn<Ocupante, LocalDate> colFechaNacimiento;
    @FXML
    private TableColumn<Reserva, Void> colAction;
    @FXML
    private TableColumn<Reserva, Integer> colOcupantes;
    @FXML
    private TableColumn<Reserva, String> colCorreo;
    @FXML
    private TableColumn<Reserva, String> colApart;

    @FXML
    private TableColumn<Reserva, String> colDocumento;

    @FXML
    private TableColumn<Reserva, EstadoReserva> colEstado;

    @FXML
    private TableColumn<Reserva, String> colTelefono;
    @FXML
    private TableView<Reserva> tableReservas;
    @FXML
    private TableView<Ocupante> tableOcupantes;
    @FXML
    private Label lblApart;

    @FXML
    private Label lblCorreo;

    @FXML
    private Label lblDescripcionApt;

    @FXML
    private Label lblDocumento;

    @FXML
    private Label lblEstadoApt;

    @FXML
    private Label lblFechaEntrada;

    @FXML
    private Label lblFechaNacimiento;

    @FXML
    private Label lblFechaSalida;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecioNoche;

    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblvalorEstancia;
    @FXML
    private ScrollPane scrollPanePrincipal;

    @FXML
    private Button buttom_Check;
    @FXML
    private Button buttom_addReserva;

    @FXML
    private Button buttom_apartamento;

    @FXML
    private Button buttom_folio;

    @FXML
    private Button buttom_inicioRecepcion;

    @FXML
    private Button buttom_reservaDirecta;

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
    public static ObservableList<Reserva> listaReservas;

    @FXML
    private void mostrarPanelCheck(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.CHECK_PANEL);
    }

    @FXML
    private void mostrarPanelFolio(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.FOLIO_PANEL);
    }

    @FXML
    void mostrarPanelApartamento(ActionEvent event) {
        Navegacion.cambiarEscena(event, Paths.APARTAMENTORECEPCION_PANEL);
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
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
    @FXML
    void crearReserva(ActionEvent event){
        Navegacion.abrirModal(event, Paths.ADDRESERVADIRECTA);
        listarReservas();
    }
    @FXML
    public void initialize(){
        if (lblFechaEntrada.getText().equals("Label")){scrollPanePrincipal.setVisible(false);}
        listaReservas=FXCollections.observableArrayList(Navegacion.getInstance().getListaReservas());
        listarReservas();
        tableReservas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        configurarColumnaAcciones();

    }
    private void listarReservas(){
        listaReservas.clear();
        listaReservas.addAll(Navegacion.getInstance().getListaReservas());
        tableReservas.setItems(listaReservas);
    }
    private void configurarColumnas(){
        tableReservas.setItems(listaReservas);
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTelefono.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva != null && reserva.getTitular() != null&&reserva.getTitular().getTelefono()!=null) {
                return new SimpleStringProperty(reserva.getTitular().getTelefono());
            }
            return new SimpleStringProperty("");
        });
        colApart.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva != null && reserva.getApartamento() != null&&reserva.getApartamento().getId()!=null) {
                return new SimpleStringProperty(reserva.getApartamento().getId());
            }
            return new SimpleStringProperty("");
        });
        colCorreo.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            if (reserva != null && reserva.getTitular() != null&&reserva.getTitular().getCorreo()!=null) {
                return new SimpleStringProperty(reserva.getTitular().getCorreo());
            }
            return new SimpleStringProperty("");
        });
        colOcupantes.setCellValueFactory(new PropertyValueFactory<>("cantidadOcupantes"));
    }
    private void configurarColumnaAcciones() {
        colAction.setCellFactory(param -> new TableCell<Reserva, Void>() {
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();
            private final HBox contenedor = new HBox(8, btnEditar, btnEliminar);
            {
                try {
                    String urlEditar = getClass().getResource("/image/modificar.png").toExternalForm();
                    String urlEliminar = getClass().getResource("/image/eliminar.png").toExternalForm();

                    // Crear los ImageView y definir un tamaño adecuado (ej: 16x16 px)
                    ImageView viewEditar = new ImageView(urlEditar);
                    viewEditar.setFitWidth(16);
                    viewEditar.setFitHeight(16);

                    ImageView viewEliminar = new ImageView(urlEliminar);
                    viewEliminar.setFitWidth(16);
                    viewEliminar.setFitHeight(16);

                    // Asignar las imágenes como gráfico a los botones
                    btnEditar.setGraphic(viewEditar);
                    btnEliminar.setGraphic(viewEliminar);
                } catch (Exception e) {
                    // Fallback por si la imagen no se encuentra
                    btnEditar.setText("Editar");
                    btnEliminar.setText("Eliminar");
                }

                // Acciones al hacer clic
                btnEditar.setOnAction(event -> {
                    Reserva reserva = getTableView().getItems().get(getIndex());
                    modificarReserva(reserva, event);
                });

                btnEliminar.setOnAction(event -> {
                    Reserva reserva = getTableView().getItems().get(getIndex());
                    eliminarReserva(reserva);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedor);
                }
            }
        });
    }
    private void modificarReserva(Reserva reserva, ActionEvent event) {
        Navegacion.getInstance().setReservarEditar(reserva);
        Navegacion.abrirModal(event, Paths.ADDRESERVADIRECTA);
        Navegacion.getInstance().setReservarEditar(null);
        tableReservas.refresh();
    }

    private void eliminarReserva(Reserva reserva) {
        if (reserva == null) return;

        // Confirmación previa
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Deseas eliminar la reserva a nombre de " + reserva.getTitular().getNombre() + "?");
        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                Navegacion.getInstance().getListaReservas().remove(reserva);
                tableReservas.getItems().remove(reserva);
                scrollPanePrincipal.setVisible(false);
                Navegacion.enviarAlertaInfo("¡Reserva eliminada con éxito!");
            }
        });
    }
    private Integer idReservaInterna;
    @FXML
    private void mostrarDatos(){
        var reserva=tableReservas.getSelectionModel().getSelectedItem();
        if (reserva!=null){
            scrollPanePrincipal.setVisible(true);
            lblNombre.setText("Nombre del titular: "+reserva.getTitular().getNombre());
            lblFechaEntrada.setText("Fecha de entrada: "+reserva.getEstancia().getFechaEntrada().toString());
            lblFechaSalida.setText("Fecha de salida: "+reserva.getEstancia().getFechaSalida().toString());
            lblvalorEstancia.setText("Anticipo congelado: "+reserva.getValorAlojamientoCongelado());
            lblCorreo.setText("Correo del titular: "+reserva.getTitular().getCorreo());
            lblTelefono.setText("Telefono del titular: "+reserva.getTitular().getTelefono());
            lblDocumento.setText("Documento del titular: "+reserva.getTitular().getDocumento());
            lblFechaNacimiento.setText("Fecha de nacimiento: "+reserva.getTitular().getFechaNacimiento().toString());
            lblApart.setText(reserva.getApartamento().getNombre()+" - "+ reserva.getApartamento().getId());
            lblPrecioNoche.setText("Precio por noche: "+reserva.getApartamento().getPrecioNoche());
            lblEstadoApt.setText("Estado del apartamento: "+reserva.getApartamento().getEstadoOperativo());
            lblDescripcionApt.setText("Descripcion del apartamento: "+reserva.getApartamento().getCaracteristicas());
            ObservableList<Ocupante> ocupantes=FXCollections.observableArrayList(reserva.getOcupantes());
            tableOcupantes.setItems(ocupantes);
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        }
    }
}
