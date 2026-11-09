package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.controller.DatosReserva;
import Aparta_Suites_UQ.controller.comunes.AddReservaController;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.utils.Paths;
import Aparta_Suites_UQ.utils.Vista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static Aparta_Suites_UQ.model.reserva.EstadoReserva.*;

/**
 * Formulario de reserva directa. Solo lee los campos y muestra mensajes;
 * las reglas de negocio están en {@link AddReservaController}.
 */
public class AddReservaViewController {

    private final AddReservaController controller = new AddReservaController();

    @FXML private Button buttom_savedReserva;
    @FXML private ComboBox<Apartamento> cbApartamentos;
    @FXML private ComboBox<EstadoReserva> cbEstado;
    @FXML private DatePicker dateFechaEntrada;
    @FXML private DatePicker dateFechaSalida;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private TextField txtCantidadOcupantes;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDoumento;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtAnticipo;

    @FXML
    public void initialize() {
        ObservableList<EstadoReserva> opciones = FXCollections.observableArrayList(PENDIENTE, CONFIRMADA, EN_CURSO, FINALIZADA, CANCELADA, NO_SHOW);
        cargarComboEstado(opciones);
        cargarApartamentos();
        Reserva reservaAEditar = controller.getReservaEnEdicion();
        if (reservaAEditar != null) {
            cbEstado.setVisible(true);
            actualizarReserva(reservaAEditar);
        } else {
            cbEstado.setVisible(false);
        }
    }

    private void actualizarReserva(Reserva reserva) {
        dateFechaEntrada.setValue(reserva.getEstancia().getFechaEntrada());
        dateFechaSalida.setValue(reserva.getEstancia().getFechaSalida());
        cbEstado.setValue(reserva.getEstado());
        txtAnticipo.setText("0");
        if (reserva.getOcupantes() != null) {
            txtCantidadOcupantes.setText(String.valueOf(reserva.getOcupantes().size()));
            txtCantidadOcupantes.setEditable(false);
        }
        actualizarTitular(reserva.getTitular());
        if (reserva.getApartamento() != null) {
            cbApartamentos.setValue(reserva.getApartamento());
        }
    }

    private void cargarApartamentos() {
        ObservableList<Apartamento> apartamentosDisponibles = FXCollections.observableArrayList(
                controller.listarApartamentosDisponibles());
        cbApartamentos.setItems(apartamentosDisponibles);
        cbApartamentos.setCellFactory(lv -> new ListCell<Apartamento>() {
            @Override
            protected void updateItem(Apartamento apartamento, boolean empty) {
                super.updateItem(apartamento, empty);
                if (empty || apartamento == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(apartamento.getNombre());
                }
            }
        });
        cbApartamentos.setButtonCell(cbApartamentos.getCellFactory().call(null));
    }

    @FXML
    void crearReservaDirecta(ActionEvent event) {
        DatosReserva datos = leerDatos();
        try {
            controller.validarDatos(datos);

            if (controller.esEdicion()) {
                // --- MODO EDICIÓN ---
                controller.actualizarReserva(datos);
                Vista.enviarAlertaInfo("¡Reserva actualizada exitosamente!");
            } else {
                // --- MODO CREACIÓN ---
                controller.validarDisponibilidad(datos);
                int cantidad = controller.parseCantidadOcupantes(datos.cantidadOcupantes());
                List<Ocupante> ocupantes = registrarOcupantes(event, cantidad);
                Reserva reserva = controller.crearReserva(datos, ocupantes);
                Vista.enviarAlertaInfo("¡Reserva creada exitosamente con el código: " + reserva.getCodigo() + "!");
            }
            cerrarVentana();
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    /** Lee los campos del formulario sin aplicar ninguna regla de negocio. */
    private DatosReserva leerDatos() {
        return new DatosReserva(
                txtNombre.getText(),
                txtDoumento.getText(),
                txtTelefono.getText(),
                txtCorreo.getText(),
                dateFechaNacimiento.getValue(),
                dateFechaEntrada.getValue(),
                dateFechaSalida.getValue(),
                cbApartamentos.getValue(),
                txtCantidadOcupantes.getText(),
                txtAnticipo.getText(),
                cbEstado.getValue());
    }

    /** Abre un modal por cada acompañante para capturar sus datos. */
    private List<Ocupante> registrarOcupantes(ActionEvent event, int cantidad) {
        if (cantidad <= 0) {
            return List.of();
        }
        List<Ocupante> listaOcupantes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            RegistrarOcupanteViewController ocupanteView = Vista.abrirModal(event, Paths.REGISTRAROCUPANTE);
            if (ocupanteView != null && ocupanteView.getOcupanteCreado() != null) {
                listaOcupantes.add(ocupanteView.getOcupanteCreado());
            } else {
                Vista.enviarAlertaWarning("Se canceló o faltaron datos en el registro del acompañante " + (i + 1));
                break;
            }
        }
        return List.copyOf(listaOcupantes);
    }

    private void cargarComboEstado(ObservableList<EstadoReserva> opciones) {
        cbEstado.setItems(opciones);
    }

    private void actualizarTitular(Huesped huesped) {
        txtNombre.setText(huesped.getNombre());
        txtDoumento.setText(huesped.getDocumento());
        txtTelefono.setText(huesped.getTelefono());
        txtCorreo.setText(huesped.getCorreo());
        dateFechaNacimiento.setValue(huesped.getFechaNacimiento());
    }

    private void cerrarVentana() {
        Stage stageActual = (Stage) buttom_savedReserva.getScene().getWindow();
        stageActual.close();
    }
}
