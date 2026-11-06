package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.model.alojamiento.Temporada;
import Aparta_Suites_UQ.model.huesped.Huesped;
import Aparta_Suites_UQ.model.huesped.Ocupante;
import Aparta_Suites_UQ.model.reserva.CanalReserva;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.model.transacciones.Cargo;
import Aparta_Suites_UQ.model.transacciones.MedioPago;
import Aparta_Suites_UQ.model.transacciones.Pago;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static Aparta_Suites_UQ.model.reserva.EstadoReserva.*;

public class AddReservaController {

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

        Reserva reservaAEditar = Navegacion.getInstance().getReservarEditar();
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
        ObservableList<Apartamento> apartamentosDisponibles = FXCollections.observableArrayList();
        for (Apartamento apartamento : Navegacion.getInstance().getListaApartamentos()) {
            if (apartamento.getEstadoOperativo() != EstadoOperativo.FUERA_DE_SERVICIO) {
                apartamentosDisponibles.add(apartamento);
            }
        }

        Reserva reservaAEditar = Navegacion.getInstance().getReservarEditar();
        if (reservaAEditar != null && reservaAEditar.getApartamento() != null) {
            if (!apartamentosDisponibles.contains(reservaAEditar.getApartamento())) {
                apartamentosDisponibles.add(reservaAEditar.getApartamento());
            }
        }

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
        if (!validarTextField()) {
            Navegacion.enviarAlertaWarning("Debes rellenar todos los campos requeridos.");
            return;
        }

        if (!validarFechas()) {
            return;
        }

        Reserva reservaEdicion = Navegacion.getInstance().getReservarEditar();

        if (reservaEdicion != null) {
            // --- MODO EDICIÓN ---
            reservaEdicion.getEstancia().setFechaEntrada(dateFechaEntrada.getValue());
            reservaEdicion.getEstancia().setFechaSalida(dateFechaSalida.getValue());
            reservaEdicion.setApartamento(cbApartamentos.getValue());
            reservaEdicion.setEstado(cbEstado.getValue());

            Huesped titular = reservaEdicion.getTitular();
            if (titular != null) {
                titular.setNombre(txtNombre.getText().trim());
                titular.setDocumento(txtDoumento.getText().trim());
                titular.setTelefono(txtTelefono.getText().trim());
                titular.setCorreo(txtCorreo.getText().trim());
                titular.setFechaNacimiento(dateFechaNacimiento.getValue());
            }

            Navegacion.getInstance().setReservarEditar(null);
            Navegacion.enviarAlertaInfo("¡Reserva actualizada exitosamente!");

            Stage stageActual = (Stage) buttom_savedReserva.getScene().getWindow();
            stageActual.close();

        } else {
            // --- MODO CREACIÓN ---
            boolean exito = registrarReserva(event);
            if (exito) {
                Stage stageActual = (Stage) buttom_savedReserva.getScene().getWindow();
                stageActual.close();
            }
        }
    }

    private boolean verificarFechas() {
        LocalDate fechaEntrada = dateFechaEntrada.getValue();
        LocalDate fechaSalida = dateFechaSalida.getValue();
        Apartamento aptoSeleccionado = registrarApartamento();

        if (fechaEntrada == null || fechaSalida == null || aptoSeleccionado == null) {
            Navegacion.enviarAlertaWarning("Por favor seleccione las fechas de entrada/salida y el apartamento.");
            return false;
        }

        Reserva reservaAEditar = Navegacion.getInstance().getReservarEditar();

        for (Reserva reserva : Navegacion.getInstance().getListaReservas()) {
            if (reserva.getEstado() != EstadoReserva.CANCELADA && !reserva.equals(reservaAEditar)) {
                if (reserva.getApartamento() != null && reserva.getApartamento().getId().equals(aptoSeleccionado.getId()) &&
                        reserva.getEstancia().seSolapaCon(fechaEntrada, fechaSalida)) {

                    Navegacion.enviarAlertaWarning("No se puede crear la reserva porque el apartamento ya está reservado entre el " +
                            fechaEntrada + " y el " + fechaSalida + ".");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean registrarReserva(ActionEvent event) {
        if (!verificarFechas()) {
            return false;
        }

        try {
            LocalDate fechaEntrada = dateFechaEntrada.getValue();
            LocalDate fechaSalida = dateFechaSalida.getValue();
            Estancia estancia = new Estancia(fechaEntrada, fechaSalida);

            int cantidadOcupantes = Integer.parseInt(txtCantidadOcupantes.getText().trim());
            List<Ocupante> ocupantes = registrarOcupantes(event, cantidadOcupantes);
            List<Temporada> temporadas = Navegacion.getInstance().getListaTemporadas();
            CanalReserva canalReserva = CanalReserva.DIRECTA;
            Huesped huesped = registrarTitular();
            Apartamento apartamento = registrarApartamento();

            double valorCalculadoEstancia = estancia.calcularValorTotal(apartamento, ocupantes, temporadas);
            Reserva reserva = new Reserva(estancia, canalReserva, huesped, apartamento, ocupantes, valorCalculadoEstancia);

            Cargo cargoAlojamiento = new Cargo("Alojamiento reserva (" + estancia.getNoches() + " noches)", valorCalculadoEstancia);
            reserva.getFolio().agregarCargo(cargoAlojamiento);

            String textoAnticipo = txtAnticipo.getText() != null ? txtAnticipo.getText().trim() : "";
            if (!textoAnticipo.isEmpty()) {
                double anticipo = Double.parseDouble(textoAnticipo);
                if (anticipo > 0) {
                    reserva.getFolio().registrarPago(new Pago(anticipo, MedioPago.EFECTIVO));
                }
            }

            Navegacion.getInstance().getListaReservas().add(reserva);
            Navegacion.enviarAlertaInfo("¡Reserva creada exitosamente con el código: " + reserva.getCodigo() + "!");
            return true;

        } catch (NumberFormatException e) {
            Navegacion.enviarAlertaWarning("Verifique que los campos numéricos tengan valores válidos.");
        } catch (Exception e) {
            Navegacion.enviarAlertaWarning("Error al crear la reserva: " + e.getMessage());
        }
        return false;
    }

    private List<Ocupante> registrarOcupantes(ActionEvent event, int cantidad) {
        if (cantidad <= 0) {
            return List.of(); // Si no hay acompañantes, retorna una lista inmutable vacía sin new ArrayList
        }

        List<Ocupante> listaOcupantes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            RegistrarOcupanteController controller = Navegacion.abrirModal(event, Paths.REGISTRAROCUPANTE);
            if (controller != null && controller.getOcupanteCreado() != null) {
                listaOcupantes.add(controller.getOcupanteCreado());
            } else {
                Navegacion.enviarAlertaWarning("Se canceló o faltaron datos en el registro del acompañante " + (i + 1));
                break;
            }
        }
        return List.copyOf(listaOcupantes); // Retorna la lista inmutable con los ocupantes agregados
    }

    private boolean validarTextField() {
        return !txtCantidadOcupantes.getText().trim().isEmpty() &&
                !txtCorreo.getText().trim().isEmpty() &&
                !txtDoumento.getText().trim().isEmpty() &&
                !txtNombre.getText().trim().isEmpty() &&
                !txtTelefono.getText().trim().isEmpty() &&
                dateFechaEntrada.getValue() != null &&
                dateFechaNacimiento.getValue() != null &&
                dateFechaSalida.getValue() != null &&
                !txtAnticipo.getText().trim().isEmpty();
    }

    private void cargarComboEstado(ObservableList<EstadoReserva> opciones) {
        cbEstado.setItems(opciones);
    }

    private boolean validarFechas() {
        Period periodo = Period.between(dateFechaNacimiento.getValue(), LocalDate.now());
        if (periodo.getYears() < 18) {
            Navegacion.enviarAlertaWarning("¡Se debe ser mayor de edad para realizar una reserva!");
            return false;
        }

        LocalDate fechaEntrada = dateFechaEntrada.getValue();
        LocalDate fechaSalida = dateFechaSalida.getValue();
        if (fechaEntrada != null && fechaSalida != null) {
            if (fechaEntrada.isAfter(fechaSalida)) {
                Navegacion.enviarAlertaWarning("¡La fecha de entrada es después de la fecha de salida!");
                return false;
            } else if (fechaEntrada.isEqual(fechaSalida)) {
                Navegacion.enviarAlertaWarning("¡La fecha de entrada y salida no pueden ser iguales!");
                return false;
            }
        }
        return true;
    }

    private Huesped registrarTitular() {
        return new Huesped(
                txtNombre.getText().trim(),
                txtDoumento.getText().trim(),
                dateFechaNacimiento.getValue(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim()
        );
    }

    private void actualizarTitular(Huesped huesped) {
        txtNombre.setText(huesped.getNombre());
        txtDoumento.setText(huesped.getDocumento());
        txtTelefono.setText(huesped.getTelefono());
        txtCorreo.setText(huesped.getCorreo());
        dateFechaNacimiento.setValue(huesped.getFechaNacimiento());
    }

    private Apartamento registrarApartamento() {
        return cbApartamentos.getValue();
    }
}