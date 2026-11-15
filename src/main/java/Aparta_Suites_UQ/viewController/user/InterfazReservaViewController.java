package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.controller.BusquedaPortal;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.viewController.util.Tablas;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.user.InterfazReservaController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazReservaViewController {
    private final InterfazReservaController controller = new InterfazReservaController();
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
    void mostrarPanelPagos(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.PAGO_PANEL);
    }

    @FXML
    void mostrarPanelReserva() {

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
    @FXML private TextField txtNombre;
    @FXML private DatePicker dpNacimiento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private Label lblPerfil;
    @FXML private DatePicker dpEntrada;
    @FXML private DatePicker dpSalida;
    @FXML private TextField txtOcNombre;
    @FXML private DatePicker dpOcNacimiento;
    @FXML private TableView<Ocupante> tblOcupantes;
    @FXML private ComboBox<Apartamento> cbApartamento;
    @FXML private Label lblResumen;

    private final ObservableList<Ocupante> ocupantes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Tablas.mostrarNombres(cbApartamento);
        Tablas.columna(tblOcupantes, "Nombre", Ocupante::getNombre);
        Tablas.columna(tblOcupantes, "Nacimiento", o -> o.getFechaNacimiento().toString());
        Tablas.columna(tblOcupantes, "Edad", o -> String.valueOf(o.calcularEdad(java.time.LocalDate.now())));
        tblOcupantes.setItems(ocupantes);

        mostrarPerfil(controller.getHuespedActual());

        BusquedaPortal busqueda = controller.getUltimaBusqueda();
        if (busqueda != null) {
            dpEntrada.setValue(busqueda.entrada());
            dpSalida.setValue(busqueda.salida());
            cbApartamento.setItems(FXCollections.observableArrayList(busqueda.apartamento()));
            cbApartamento.setValue(busqueda.apartamento());
            lblResumen.setText("Elegido en la cotización: " + busqueda.apartamento().getNombre() + " para "
                    + busqueda.personas() + " persona(s). Agregue los " + (busqueda.personas() - 1)
                    + " ocupante(s) (nombre y fecha de nacimiento) antes de crear la reserva.");
        }
    }

    private void mostrarPerfil(Huesped huesped) {
        if (huesped == null) {
            lblPerfil.setText("Sin perfil activo: busque su documento o complete sus datos y guárdelos.");
            return;
        }
        txtDocumento.setText(huesped.getDocumento());
        txtNombre.setText(huesped.getNombre());
        dpNacimiento.setValue(huesped.getFechaNacimiento());
        txtTelefono.setText(huesped.getTelefono());
        txtCorreo.setText(huesped.getCorreo());
        lblPerfil.setText("Perfil activo: " + huesped.getNombre());
    }

    @FXML
    void onBuscarPerfil(ActionEvent event) {
        try {
            Huesped huesped = controller.identificarPorDocumento(txtDocumento.getText());
            if (huesped == null) {
                lblPerfil.setText("No hay un perfil con ese documento. Complete sus datos y guárdelos para registrarse.");
            } else {
                mostrarPerfil(huesped);
            }
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onGuardarPerfil(ActionEvent event) {
        try {
            mostrarPerfil(controller.guardarPerfil(txtNombre.getText(), txtDocumento.getText(),
                    dpNacimiento.getValue(), txtTelefono.getText(), txtCorreo.getText()));
            Vista.enviarAlertaInfo("Perfil guardado.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCerrarPerfil(ActionEvent event) {
        controller.cerrarPerfil();
        txtDocumento.clear();
        txtNombre.clear();
        dpNacimiento.setValue(null);
        txtTelefono.clear();
        txtCorreo.clear();
        lblPerfil.setText("Perfil cerrado.");
    }

    @FXML
    void onAgregarOcupante(ActionEvent event) {
        try {
            ocupantes.add(controller.crearOcupante(txtOcNombre.getText(), dpOcNacimiento.getValue()));
            txtOcNombre.clear();
            dpOcNacimiento.setValue(null);
            cbApartamento.setValue(null);
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onQuitarOcupante(ActionEvent event) {
        Ocupante seleccionado = tblOcupantes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            ocupantes.remove(seleccionado);
            cbApartamento.setValue(null);
        }
    }

    @FXML
    void onVerDisponibles(ActionEvent event) {
        try {
            var disponibles = controller.buscarDisponibles(dpEntrada.getValue(), dpSalida.getValue(), ocupantes.size() + 1);
            cbApartamento.setItems(FXCollections.observableArrayList(disponibles));
            lblResumen.setText(disponibles.isEmpty()
                    ? "No hay apartamentos disponibles para " + (ocupantes.size() + 1) + " persona(s) en esas fechas."
                    : disponibles.size() + " apartamento(s) disponible(s) para " + (ocupantes.size() + 1) + " persona(s).");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onCrearReserva(ActionEvent event) {
        try {
            Reserva reserva = controller.crearReserva(cbApartamento.getValue(), dpEntrada.getValue(),
                    dpSalida.getValue(), new ArrayList<>(ocupantes));
            controller.limpiarUltimaBusqueda();
            ocupantes.clear();
            cbApartamento.setValue(null);
            lblResumen.setText("");
            Vista.enviarAlertaInfo("¡Reserva " + reserva.getCodigo() + " creada! Valor de la estancia: "
                    + Tablas.dinero(reserva.getValorAlojamientoCongelado()) + ".\nQueda Pendiente: pague el anticipo de "
                    + Tablas.dinero(reserva.calcularAnticipoRequerido())
                    + " (30 %) en «Autogestión y Pagos» dentro de las próximas 24 horas o se cancelará.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }
}
