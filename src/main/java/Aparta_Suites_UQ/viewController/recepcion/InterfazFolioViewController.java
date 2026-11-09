package Aparta_Suites_UQ.viewController.recepcion;
import Aparta_Suites_UQ.controller.recepcion.InterfazFolioController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.reserva.Folio;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;
import Aparta_Suites_UQ.utils.Paths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InterfazFolioViewController {

    // --- COMPONENTES MENÚ LATERAL ---
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

    // --- COMPONENTES DE LA VISTA DE FOLIO ---
    @FXML private ComboBox<Reserva> cmbReservas;
    @FXML private Label lblInfoTitular;
    @FXML private Label lblTotalCargos;
    @FXML private Label lblTotalPagos;
    @FXML private Label lblSaldo;

    // Tabla Cargos
    @FXML private TableView<Cargo> tblCargos;
    @FXML private TableColumn<Cargo, String> colConceptoCargo;
    @FXML private TableColumn<Cargo, Double> colValorCargo;
    @FXML private TableColumn<Cargo, String> colFechaCargo;
    @FXML private TextField txtConceptoCargo;
    @FXML private TextField txtValorCargo;

    // Tabla Pagos
    @FXML private TableView<Pago> tblPagos;
    @FXML private TableColumn<Pago, MedioPago> colMedioPago;
    @FXML private TableColumn<Pago, Double> colValorPago;
    @FXML private TableColumn<Pago, String> colFechaPago;
    @FXML private ComboBox<MedioPago> cmbMedioPago;
    @FXML private TextField txtValorPago;

    private final InterfazFolioController controller = new InterfazFolioController();
    // Variables de estado
    private Reserva reservaSeleccionada;
    private Folio folioActual;

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // 1. Configurar fecha en Tabla Cargos
        colFechaCargo.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFecha();
            String fechaFormateada = (fecha != null) ? fecha.format(formatter) : "--";
            return new SimpleStringProperty(fechaFormateada);
        });
        colConceptoCargo.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colValorCargo.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // 2. Configurar fecha en Tabla Pagos
        colFechaPago.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFecha();
            String fechaFormateada = (fecha != null) ? fecha.format(formatter) : "--";
            return new SimpleStringProperty(fechaFormateada);
        });
        colMedioPago.setCellValueFactory(new PropertyValueFactory<>("medioPago"));
        colValorPago.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // 2. Configurar columna Concepto leyendo getConcepto()
        colConceptoCargo.setCellValueFactory(new PropertyValueFactory<>("concepto"));

        // 3. Configurar columna Valor leyendo getValor()
        colValorCargo.setCellValueFactory(new PropertyValueFactory<>("valor"));
        // 1. Configurar columnas de las tablas
        colConceptoCargo.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colValorCargo.setCellValueFactory(new PropertyValueFactory<>("valor"));

        colMedioPago.setCellValueFactory(new PropertyValueFactory<>("medioPago"));
        colValorPago.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // 2. Cargar medios de pago en el ComboBox
        cmbMedioPago.setItems(FXCollections.observableArrayList(MedioPago.values()));

        // 3. Formatear cómo se visualiza cada Reserva en el ComboBox de selección
        cmbReservas.setConverter(new StringConverter<Reserva>() {
            @Override
            public String toString(Reserva r) {
                if (r == null) return "";
                String nombreTitular = (r.getTitular() != null) ? r.getTitular().getNombre() : "Sin Titular";
                return "Reserva #" + r.getCodigo() + " - " + nombreTitular;
            }

            @Override
            public Reserva fromString(String string) {
                return null;
            }
        });

        // 4. Cargar lista global de reservas
        cmbReservas.setItems(FXCollections.observableArrayList(controller.listarReservas()));

        // Si ya había una reserva preseleccionada en Navegación, la cargamos automáticamente
        if (controller.getReservaPreseleccionada() != null) {
            cmbReservas.setValue(controller.getReservaPreseleccionada());
            onReservaSeleccionada();
        }
    }

    // --- LÓGICA DE FOLIO ---

    @FXML
    void onReservaSeleccionada() {
        this.reservaSeleccionada = cmbReservas.getValue();
        if (reservaSeleccionada != null) {
            this.folioActual = reservaSeleccionada.getFolio();

            String titular = (reservaSeleccionada.getTitular() != null) ? reservaSeleccionada.getTitular().getNombre() : "N/A";
            String apto = (reservaSeleccionada.getApartamento() != null) ? reservaSeleccionada.getApartamento().getNombre() : "N/A";

            lblInfoTitular.setText("Titular: " + titular + " | Apt: " + apto);
            actualizarDatosFolio();
        }
    }

    private void actualizarDatosFolio() {
        if (folioActual == null) return;

        // Cargar listas en las tablas de la interfaz
        tblCargos.setItems(FXCollections.observableArrayList(folioActual.getCargos()));
        tblPagos.setItems(FXCollections.observableArrayList(folioActual.getPagos()));

        // Actualizar métricas financieras en COP (sin decimales)
        lblTotalCargos.setText(String.format("$ %,.0f", folioActual.getTotalCargos()));
        lblTotalPagos.setText(String.format("$ %,.0f", folioActual.getTotalPagos()));

        double saldo = folioActual.getSaldo();
        lblSaldo.setText(String.format("$ %,.0f", saldo));

        // Resaltar saldo: Verde si no debe o está a favor, Rojo si hay deuda
        lblSaldo.getStyleClass().removeAll("saldo-positivo", "saldo-deuda");
        lblSaldo.getStyleClass().add(saldo <= 0 ? "saldo-positivo" : "saldo-deuda");
    }

    @FXML
    void onAgregarCargo(ActionEvent event) {
        if (validarReservaSeleccionada()) return;
        try {
            controller.agregarCargo(folioActual, txtConceptoCargo.getText(), txtValorCargo.getText());
            txtConceptoCargo.clear();
            txtValorCargo.clear();
            actualizarDatosFolio();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onRegistrarPago(ActionEvent event) {
        if (validarReservaSeleccionada()) return;
        try {
            controller.registrarPago(folioActual, cmbMedioPago.getValue(), txtValorPago.getText());
            txtValorPago.clear();
            cmbMedioPago.setValue(null);
            actualizarDatosFolio();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCorreccionCargo(ActionEvent event) {
        if (validarReservaSeleccionada()) return;
        try {
            controller.registrarCorreccionCargo(folioActual, txtConceptoCargo.getText(), txtValorCargo.getText());
            txtConceptoCargo.clear();
            txtValorCargo.clear();
            actualizarDatosFolio();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCorreccionPago(ActionEvent event) {
        if (validarReservaSeleccionada()) return;
        try {
            controller.registrarCorreccionPago(folioActual, cmbMedioPago.getValue(), txtValorPago.getText());
            txtValorPago.clear();
            cmbMedioPago.setValue(null);
            actualizarDatosFolio();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    private boolean validarReservaSeleccionada() {
        if (folioActual == null) {
            Vista.enviarAlertaWarning("Debe seleccionar una reserva en el desplegable primero.");
            return true;
        }
        return false;
    }

    // --- NAVEGACIÓN Y MENÚ LATERAL ---

    @FXML
    void mostrarPanelCheck(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.CHECK_PANEL);
    }

    @FXML
    void mostrarPanelFolio() {
        // Ya se encuentra en la vista Folio
    }

    @FXML
    void mostrarPanelApartamento(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.APARTAMENTORECEPCION_PANEL);
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.RESERVADIRECTA_PANEL);
    }

    @FXML
    private void mostrarPanelInicioRecepcion(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.RECEPCION_PANEL);
    }

    // --- EFECTOS MOUSE (HOVER) MENÚ LATERAL ---

    @FXML
    void vistaMouseAfueraFolio(MouseEvent event) {
        // Mantener visible si se prefiere indicar que es la vista activa
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
}