package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;
import Aparta_Suites_UQ.viewController.util.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.user.InterfazPagoController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazPagoViewController {
    private final InterfazPagoController controller = new InterfazPagoController();
    @FXML
    private Button buttom_Cotizacion;

    @FXML
    private Button buttom_Pagos;

    @FXML
    private Button buttom_inicioUsuario;

    @FXML
    private Button buttom_reserva;

    @FXML
    private Rectangle rect_cotizacion_buttom;

    @FXML
    private Rectangle rect_inicio_buttom;

    @FXML
    private Rectangle rect_pagos_buttom;

    @FXML
    private Rectangle rect_reserva_buttom;

    @FXML
    void mostrarPanelContizacion(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.COTIZACION_PANEL);
    }

    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.USUARIO_PANEL);
    }

    @FXML
    void mostrarPanelPagos() {
    }

    @FXML
    void mostrarPanelReserva(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.RESERVA_PANEL);
    }

    @FXML
    void vistaMouseAfueraCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraPagos(MouseEvent event) {
        rect_pagos_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseAfueraReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(false);
    }

    @FXML
    void vistaMouseEncimaCotizacion(MouseEvent event) {
        rect_cotizacion_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaInicio(MouseEvent event) {
        rect_inicio_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaPagos(MouseEvent event) {
        rect_pagos_buttom.setVisible(true);
    }

    @FXML
    void vistaMouseEncimaReserva(MouseEvent event) {
        rect_reserva_buttom.setVisible(true);
    }

    @FXML private TextField txtDocumento;
    @FXML private Label lblHuesped;
    @FXML private Label lblDetalle;
    @FXML private TableView<Reserva> tblMisReservas;
    @FXML private ComboBox<MedioPago> cbMedio;
    @FXML private TextField txtValor;
    @FXML private TableView<Pago> tblPagos;

    @FXML
    public void initialize() {
        cbMedio.setItems(FXCollections.observableArrayList(MedioPago.values()));
        Tablas.columna(tblMisReservas, "Código", Reserva::getCodigo);
        Tablas.columna(tblMisReservas, "Apartamento", r -> r.getApartamento().getNombre());
        Tablas.columna(tblMisReservas, "Entrada", r -> r.getEstancia().getFechaEntrada().toString());
        Tablas.columna(tblMisReservas, "Salida", r -> r.getEstancia().getFechaSalida().toString());
        Tablas.columna(tblMisReservas, "Estado", r -> r.getEstado().name());
        Tablas.columna(tblMisReservas, "Valor", r -> Tablas.dinero(r.getFolio().getTotalCargos()));
        Tablas.columna(tblMisReservas, "Pagado", r -> Tablas.dinero(r.getFolio().getTotalPagos()));
        Tablas.columna(tblMisReservas, "Saldo", r -> Tablas.dinero(r.getFolio().getSaldo()));
        tblMisReservas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, r) -> mostrarDetalle(r));

        Tablas.columna(tblPagos, "Fecha", p -> p.getFecha().toLocalDate().toString());
        Tablas.columna(tblPagos, "Medio", p -> p.getMedioPago().name());
        Tablas.columna(tblPagos, "Valor", p -> Tablas.dinero(p.getValor()));

        Huesped actual = controller.getHuespedActual();
        if (actual != null) {
            txtDocumento.setText(actual.getDocumento());
            cargarReservas();
        }
    }

    private void cargarReservas() {
        Huesped huesped = controller.getHuespedActual();
        lblHuesped.setText(huesped == null ? "" : "Huésped: " + huesped.getNombre());
        tblMisReservas.setItems(FXCollections.observableArrayList(controller.listarMisReservas()));
        tblMisReservas.refresh();
        mostrarDetalle(tblMisReservas.getSelectionModel().getSelectedItem());
    }

    private void mostrarDetalle(Reserva reserva) {
        if (reserva == null) {
            lblDetalle.setText("Seleccione una reserva.");
            tblPagos.getItems().clear();
            return;
        }
        StringBuilder texto = new StringBuilder("Reserva " + reserva.getCodigo() + " — " + reserva.getEstado()
                + "\nSaldo: " + Tablas.dinero(reserva.getFolio().getSaldo()));
        if (reserva.getEstado() == EstadoReserva.PENDIENTE) {
            texto.append("\nPara confirmar pague al menos ").append(Tablas.dinero(reserva.calcularAnticipoRequerido()))
                    .append(" (30 %).");
        }
        lblDetalle.setText(texto.toString());
        tblPagos.setItems(FXCollections.observableArrayList(reserva.getFolio().getPagos()));
    }

    @FXML
    void onVerMisReservas(ActionEvent event) {
        try {
            Huesped huesped = controller.identificarPorDocumento(txtDocumento.getText());
            if (huesped == null) {
                tblMisReservas.getItems().clear();
                lblHuesped.setText("");
                Vista.enviarAlertaWarning("No existe un perfil con ese documento. Regístrelo en la pantalla «Reserva».");
                return;
            }
            cargarReservas();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onPagar(ActionEvent event) {
        Reserva reserva = tblMisReservas.getSelectionModel().getSelectedItem();
        try {
            boolean confirmada = controller.pagar(reserva, cbMedio.getValue(), txtValor.getText());
            txtValor.clear();
            cbMedio.setValue(null);
            cargarReservas();
            Vista.enviarAlertaInfo(confirmada
                    ? "Pago registrado. ¡Su reserva quedó CONFIRMADA!"
                    : "Pago registrado correctamente.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCancelarReserva(ActionEvent event) {
        Reserva reserva = tblMisReservas.getSelectionModel().getSelectedItem();
        try {
            String previsto = controller.previsualizarCancelacion(reserva);
            if (Vista.enviarAlertaConfirmacion("Si cancela ahora:\n" + previsto + "\n\n¿Desea cancelar la reserva "
                    + reserva.getCodigo() + "?")) {
                String resultado = controller.cancelar(reserva);
                cargarReservas();
                Vista.enviarAlertaInfo("Reserva cancelada.\n" + resultado);
            }
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }
}
