package Aparta_Suites_UQ.viewController.recepcion;
import Aparta_Suites_UQ.controller.recepcion.InterfazCheckController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.utils.Paths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class InterfazCheckViewController {
    @FXML private Button buttom_Check;
    @FXML private Button buttom_apartamento;
    @FXML private Button buttom_folio;
    @FXML private Button buttom_inicioRecepcion;
    @FXML private Button buttom_reservaDirecta;

    @FXML private Rectangle rect_apartamento_buttom;
    @FXML private Rectangle rect_check_buttom;
    @FXML private Rectangle rect_folio_buttom;
    @FXML private Rectangle rect_inicio_buttom;
    @FXML private Rectangle rect_reserva_buttom;
    @FXML private TextField txtBuscarReserva;
    @FXML private ComboBox<String> cmbFiltroEstado;

    @FXML private TableView<Reserva> tblReservas;
    @FXML private TableColumn<Reserva, String> colCodigo;
    @FXML private TableColumn<Reserva, String> colTitular;
    @FXML private TableColumn<Reserva, String> colApartamento;
    @FXML private TableColumn<Reserva, String> colEntrada;
    @FXML private TableColumn<Reserva, String> colSalida;
    @FXML private TableColumn<Reserva, EstadoReserva> colEstado;

    @FXML private Label lblCodigoReserva;
    @FXML private Label lblEstadoApto;
    @FXML private Label lblSaldoFolio;

    @FXML private Button btnCheckIn;
    @FXML private Button btnCheckOut;
    @FXML private Button btnNoShow;

    private final InterfazCheckController controller = new InterfazCheckController();
    private Reserva reservaSeleccionada;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        // 1. Configurar columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTitular.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitular() != null ? cellData.getValue().getTitular().getNombre() : "--"));
        colApartamento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getApartamento() != null ? cellData.getValue().getApartamento().getNombre() : "--"));
        colEntrada.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstancia() != null ? cellData.getValue().getEstancia().getFechaEntrada().format(dateFormatter) : "--"));
        colSalida.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstancia() != null ? cellData.getValue().getEstancia().getFechaSalida().format(dateFormatter) : "--"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "PENDIENTE", "CONFIRMADA", "EN_CURSO", "FINALIZADA", "CANCELADA", "NO_SHOW"
        ));
        cmbFiltroEstado.setValue("Todos");
        tblReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            this.reservaSeleccionada = newSel;
            actualizarDetallesSeleccion();
        });
        onRefrescarLista();
    }
    @FXML
    void onRefrescarLista() {
        List<Reserva> listaGlobal = controller.refrescarReservas();
        tblReservas.setItems(FXCollections.observableArrayList(listaGlobal));
        actualizarDetallesSeleccion();
    }

    @FXML
    void onFiltrarReservas() {
        List<Reserva> filtrada = controller.filtrarReservas(txtBuscarReserva.getText(), cmbFiltroEstado.getValue());
        tblReservas.setItems(FXCollections.observableArrayList(filtrada));
    }

    private void actualizarDetallesSeleccion() {
        if (reservaSeleccionada == null) {
            lblCodigoReserva.setText("Reserva: --");
            lblEstadoApto.setText("Estado Apt: --");
            lblSaldoFolio.setText("Saldo Folio: --");
            btnCheckIn.setDisable(true);
            btnCheckOut.setDisable(true);
            btnNoShow.setDisable(true);
            return;
        }

        lblCodigoReserva.setText("Reserva #" + reservaSeleccionada.getCodigo() + " (" + reservaSeleccionada.getEstado() + ")");

        if (reservaSeleccionada.getApartamento() != null) {
            lblEstadoApto.setText("Estado Apt: " + reservaSeleccionada.getApartamento().getEstadoOperativo());
        }

        if (reservaSeleccionada.getFolio() != null) {
            double saldo = reservaSeleccionada.getFolio().getSaldo();
            lblSaldoFolio.setText(String.format("Saldo Folio: $ %,.0f", saldo));
            lblSaldoFolio.getStyleClass().removeAll("saldo-folio-positivo", "saldo-folio-deuda");
            lblSaldoFolio.getStyleClass().add(saldo <= 0 ? "saldo-folio-positivo" : "saldo-folio-deuda");
        }
        btnCheckIn.setDisable(!controller.puedeCheckIn(reservaSeleccionada));
        btnCheckOut.setDisable(!controller.puedeCheckOut(reservaSeleccionada));
        btnNoShow.setDisable(!controller.puedeDeclararNoShow(reservaSeleccionada));
    }
    @FXML
    void onRealizarCheckIn(ActionEvent event) {
        if (reservaSeleccionada == null) return;

        try {
            if (controller.realizarCheckIn(reservaSeleccionada)) {
                Vista.enviarAlertaInfo("Check-In realizado exitosamente para la Reserva #" + reservaSeleccionada.getCodigo());
                onRefrescarLista();
            } else {
                Vista.enviarAlertaWarning("No se puede realizar el Check-In. Verifique la hora de entrada y el estado de la reserva.");
            }
        } catch (Exception e) {
            Vista.enviarAlertaWarning("Error en Check-In: " + e.getMessage());
        }
    }

    @FXML
    void onRealizarCheckOut(ActionEvent event) {
        if (reservaSeleccionada == null) return;

        try {
            controller.realizarCheckOut(reservaSeleccionada);
            Vista.enviarAlertaInfo("Check-Out exitoso. El apartamento pasa a estado EN_PREPARACIÓN.");
            onRefrescarLista();
        } catch (IllegalStateException e) {
            // Disparado si el Folio tiene saldo pendiente
            Vista.enviarAlertaWarning(e.getMessage());
        } catch (Exception e) {
            Vista.enviarAlertaWarning("Error en Check-Out: " + e.getMessage());
        }
    }

    @FXML
    void onDeclararNoShow(ActionEvent event) {
        if (reservaSeleccionada == null) return;

        try {
            controller.declararNoShow(reservaSeleccionada);
            Vista.enviarAlertaInfo("Reserva declarada como NO-SHOW correctamente.");
            onRefrescarLista();
        } catch (Exception e) {
            Vista.enviarAlertaWarning("Error al declarar No-Show: " + e.getMessage());
        }
    }

    @FXML
    void onIrAFolio(ActionEvent event) {
        if (reservaSeleccionada != null) {
            controller.seleccionarReservaParaFolio(reservaSeleccionada);
            Vista.cambiarEscena(event, Paths.FOLIO_PANEL);
        } else {
            Vista.enviarAlertaWarning("Seleccione una reserva de la tabla primero.");
        }
    }
    @FXML void mostrarPanelCheck() {}
    @FXML void mostrarPanelFolio(ActionEvent event) { Vista.cambiarEscena(event, Paths.FOLIO_PANEL); }
    @FXML void mostrarPanelApartamento(ActionEvent event) { Vista.cambiarEscena(event, Paths.APARTAMENTORECEPCION_PANEL); }
    @FXML void mostrarPanelReserva(ActionEvent event) { Vista.cambiarEscena(event, Paths.RESERVADIRECTA_PANEL); }
    @FXML void mostrarPanelInicioRecepcion(ActionEvent event) { Vista.cambiarEscena(event, Paths.RECEPCION_PANEL); }

    @FXML void vistaMouseAfueraFolio(MouseEvent event) { rect_folio_buttom.setVisible(false); }
    @FXML void vistaMouseAfueraApartamento(MouseEvent event) { rect_apartamento_buttom.setVisible(false); }
    @FXML void vistaMouseAfueraInicio(MouseEvent event) { rect_inicio_buttom.setVisible(false); }
    @FXML void vistaMouseAfueraCheck(MouseEvent event) {}
    @FXML void vistaMouseAfueraReserva(MouseEvent event) { rect_reserva_buttom.setVisible(false); }

    @FXML void vistaMouseEncimaFolio(MouseEvent event) { rect_folio_buttom.setVisible(true); }
    @FXML void vistaMouseEncimaApartamento(MouseEvent event) { rect_apartamento_buttom.setVisible(true); }
    @FXML void vistaMouseEncimaInicio(MouseEvent event) { rect_inicio_buttom.setVisible(true); }
    @FXML void vistaMouseEncimaCheck(MouseEvent event) { rect_check_buttom.setVisible(true); }
    @FXML void vistaMouseEncimaReserva(MouseEvent event) { rect_reserva_buttom.setVisible(true); }
}