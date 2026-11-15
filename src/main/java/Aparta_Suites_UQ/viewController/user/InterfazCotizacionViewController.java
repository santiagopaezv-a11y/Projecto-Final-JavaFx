package Aparta_Suites_UQ.viewController.user;
import Aparta_Suites_UQ.controller.Cotizacion;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.NocheLiquidada;
import Aparta_Suites_UQ.viewController.util.Tablas;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Aparta_Suites_UQ.controller.user.InterfazCotizacionController;

import Aparta_Suites_UQ.viewController.util.Vista;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class InterfazCotizacionViewController {
    private final InterfazCotizacionController controller = new InterfazCotizacionController();
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
    void mostrarPanelCotizacion(ActionEvent event) {
    }

    @FXML
    void mostrarPanelInicioUsuario(ActionEvent event) {
        Vista.cambiarEscena(event,Paths.USUARIO_PANEL);
    }

    @FXML
    void mostrarPanelPagos(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.PAGO_PANEL);
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

    @FXML private DatePicker dpEntrada;
    @FXML private DatePicker dpSalida;
    @FXML private TextField txtPersonas;
    @FXML private TextField txtMenores;
    @FXML private Label lblResumen;
    @FXML private Label lblTotal;
    @FXML private TableView<Apartamento> tblDisponibles;
    @FXML private TableView<NocheLiquidada> tblDesglose;

    // Parámetros de la última búsqueda (la cotización usa estos, no lo que se edite después)
    private LocalDate entradaBuscada;
    private LocalDate salidaBuscada;
    private int personasBuscadas;
    private int menoresBuscados;

    @FXML
    public void initialize() {
        Tablas.columna(tblDisponibles, "Apartamento", Apartamento::getNombre);
        Tablas.columna(tblDisponibles, "Dorm.", a -> String.valueOf(a.getNumeroDormitorios()));
        Tablas.columna(tblDisponibles, "Capacidad", a -> String.valueOf(a.getCantMaximaPersonas()));
        Tablas.columna(tblDisponibles, "Características", Apartamento::getCaracteristicas);
        Tablas.columna(tblDisponibles, "Total estancia", a -> {
            try {
                return Tablas.dinero(cotizar(a).total());
            } catch (IllegalArgumentException e) {
                return "-";
            }
        });
        Tablas.columna(tblDesglose, "Noche", n -> n.fecha().toString());
        Tablas.columna(tblDesglose, "Temporada", n -> n.temporada().name());
        Tablas.columna(tblDesglose, "Tarifa", n -> Tablas.dinero(n.tarifa()));
        Tablas.columna(tblDesglose, "Pagan", n -> String.valueOf(n.personasQuePagan()));
        Tablas.columna(tblDesglose, "Subtotal", n -> Tablas.dinero(n.subtotal()));
        tblDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, anterior, apto) -> mostrarCotizacion(apto));
        txtPersonas.setText("2");
        txtMenores.setText("0");
    }

    private Cotizacion cotizar(Apartamento apartamento) {
        return controller.cotizar(apartamento, entradaBuscada, salidaBuscada, personasBuscadas, menoresBuscados);
    }

    private int entero(TextField campo, String nombre) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombre + " debe ser un número entero.");
        }
    }

    @FXML
    void onBuscar(ActionEvent event) {
        try {
            int personas = entero(txtPersonas, "El número de personas");
            int menores = entero(txtMenores, "El número de menores de 6 años");
            var disponibles = controller.buscarDisponibles(dpEntrada.getValue(), dpSalida.getValue(), personas);
            entradaBuscada = dpEntrada.getValue();
            salidaBuscada = dpSalida.getValue();
            personasBuscadas = personas;
            menoresBuscados = menores;
            tblDisponibles.setItems(FXCollections.observableArrayList(disponibles));
            tblDesglose.getItems().clear();
            lblTotal.setText("");
            lblResumen.setText(disponibles.isEmpty()
                    ? "No hay apartamentos disponibles para esas fechas y ese número de personas."
                    : disponibles.size() + " apartamento(s) disponible(s). Seleccione uno para ver el desglose.");
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    private void mostrarCotizacion(Apartamento apartamento) {
        if (apartamento == null) {
            return;
        }
        try {
            Cotizacion cotizacion = cotizar(apartamento);
            tblDesglose.setItems(FXCollections.observableArrayList(cotizacion.noches()));
            lblTotal.setText("Total: " + Tablas.dinero(cotizacion.total()) + " (" + cotizacion.noches().size()
                    + " noches, " + cotizacion.personasQuePagan() + " persona(s) pagan).\nAnticipo del 30 % para confirmar: "
                    + Tablas.dinero(cotizacion.anticipoPortal()));
        } catch (IllegalArgumentException e) {
            Vista.enviarAlertaWarning(e.getMessage());
        }
    }

    @FXML
    void onReservar(ActionEvent event) {
        Apartamento seleccionado = tblDisponibles.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Vista.enviarAlertaWarning("Seleccione un apartamento de la tabla.");
            return;
        }
        controller.guardarSeleccion(seleccionado, entradaBuscada, salidaBuscada, personasBuscadas, menoresBuscados);
        Vista.cambiarEscena(event, Paths.RESERVA_PANEL);
    }
}
