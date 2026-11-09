package Aparta_Suites_UQ.viewController.recepcion;
import Aparta_Suites_UQ.controller.recepcion.InterfazRecepcionController;

import Aparta_Suites_UQ.utils.Vista;
import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.reserva.EstadoReserva;
import Aparta_Suites_UQ.model.reserva.Estancia;
import Aparta_Suites_UQ.model.reserva.Reserva;
import Aparta_Suites_UQ.utils.Mes;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

import java.time.YearMonth;
import java.util.List;

public class InterfazRecepcionViewController {
    private final InterfazRecepcionController controller = new InterfazRecepcionController();
    @FXML
    private Button buttom_Check;
    @FXML
    private Button buttom_accesModulo;
    @FXML
    private Button buttom_apartamento;
    @FXML
    private Button buttom_changeUsuario;
    @FXML
    private Button buttom_folio;
    @FXML
    private Button buttom_inicioRecepcion;
    @FXML
    private Button buttom_reservaDirecta;
    @FXML
    private Label lblMesActual;
    @FXML
    private GridPane gridCalendario;
    @FXML
    private TextField txtContraseñaUsuario;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private AnchorPane panelChangeUsuario;
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

    @FXML
    void mostrarPanelCheck(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.CHECK_PANEL);
    }

    @FXML
    void mostrarPanelFolio(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.FOLIO_PANEL);
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
    private void mostrarPanelGestionUsuario() {
        panelChangeUsuario.setVisible(true);
    }

    @FXML
    private void ocultarPanelAlClickearFuera(MouseEvent event) {
        if (panelChangeUsuario.isVisible() && !panelChangeUsuario.getBoundsInParent().contains(event.getX(), event.getY())) {
            panelChangeUsuario.setVisible(false);
        }
    }

    @FXML
    private void initialize() {
        refrescarTabla();
        gridCalendario.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                refrescarTabla();
            }
        });
    }

    @FXML
    private void actualizarTabla(ActionEvent event) {
        refrescarTabla();
    }

    public void refrescarTabla() {
        YearMonth mesActual = YearMonth.now();
        construirTablaReservas(mesActual);
    }

    @FXML
    private void cambiarModuloUser(ActionEvent event) {
        Vista.cambiarEscena(event, Paths.USUARIO_PANEL);
    }

    @FXML
    private void mostrarPanelInicioRecepcion(ActionEvent event) {
    }

    @FXML
    private void cambiarModuloAdmin(ActionEvent event) {
        if (controller.autenticarAdministrador(txtNombreUsuario.getText(), txtContraseñaUsuario.getText())) {
            Vista.cambiarEscena(event, Paths.ADMINPANEL);
            return;
        } else {
            Vista.enviarAlerta(Alert.AlertType.WARNING, "Advertencia del Sistema", null, "El usuario o contrasena esta incorrecto");
        }
        panelChangeUsuario.setVisible(false);
    }

    private String claseEstadoReserva(EstadoReserva estado) {
        if (estado == null) return "reserva-pendiente";
        switch (estado) {
            case EN_CURSO: return "reserva-en-curso";
            case FINALIZADA: return "reserva-finalizada";
            case CONFIRMADA: return "reserva-confirmada";
            case CANCELADA: return "reserva-cancelada";
            case NO_SHOW: return "reserva-no-show";
            default: return "reserva-pendiente";
        }
    }

    public void construirTablaReservas(YearMonth mesActual) {
        lblMesActual.setText(Mes.fromNumero(mesActual.getMonthValue()).toString().toLowerCase());
        int totalDiasMes = mesActual.lengthOfMonth();
        double ANCHO_APARTO = 110.0;
        double ANCHO_DIA = 40.0;
        double ALTO_FILA = 40.0;

        gridCalendario.getChildren().clear();
        gridCalendario.getColumnConstraints().clear();
        gridCalendario.getRowConstraints().clear();

        // Fondo blanco general para el grid
        gridCalendario.getStyleClass().add("calendario-grid");

        // Anchos de columna
        gridCalendario.getColumnConstraints().add(new ColumnConstraints(ANCHO_APARTO));
        for (int dia = 1; dia <= totalDiasMes; dia++) {
            gridCalendario.getColumnConstraints().add(new ColumnConstraints(ANCHO_DIA));
        }

        // FILA 0: Encabezados
        gridCalendario.getRowConstraints().add(new RowConstraints(35.0));

        Label lblHeaderApto = new Label("Apartamento");
        lblHeaderApto.setMaxWidth(Double.MAX_VALUE);
        lblHeaderApto.setMaxHeight(Double.MAX_VALUE);
        lblHeaderApto.setAlignment(javafx.geometry.Pos.CENTER);
        lblHeaderApto.getStyleClass().add("calendario-encabezado");
        gridCalendario.add(lblHeaderApto, 0, 0);

        for (int dia = 1; dia <= totalDiasMes; dia++) {
            Label lblDia = new Label(String.valueOf(dia));
            lblDia.setMaxWidth(Double.MAX_VALUE);
            lblDia.setMaxHeight(Double.MAX_VALUE);
            lblDia.setAlignment(javafx.geometry.Pos.CENTER);
            lblDia.getStyleClass().addAll("calendario-encabezado", "calendario-dia");
            gridCalendario.add(lblDia, dia, 0);
        }

        // FILAS DE APARTAMENTOS
        List<Apartamento> listaAptos = controller.listarApartamentos();
        List<Reserva> listaReservas = controller.listarReservas();

        for (int i = 0; i < listaAptos.size(); i++) {
            Apartamento apto = listaAptos.get(i);
            int filaActual = i + 1;

            gridCalendario.getRowConstraints().add(new RowConstraints(ALTO_FILA));

            // Columna 0: Apartamento con bordes
            Label lblApto = new Label(apto.getId());
            lblApto.setMaxWidth(Double.MAX_VALUE);
            lblApto.setMaxHeight(Double.MAX_VALUE);
            lblApto.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            lblApto.getStyleClass().add("calendario-apto");
            gridCalendario.add(lblApto, 0, filaActual);

            // Crear celdas de fondo vacías con bordes suaves para cada día
            for (int dia = 1; dia <= totalDiasMes; dia++) {
                Label celdaVacia = new Label();
                celdaVacia.setMaxWidth(Double.MAX_VALUE);
                celdaVacia.setMaxHeight(Double.MAX_VALUE);
                celdaVacia.getStyleClass().add("calendario-celda");
                gridCalendario.add(celdaVacia, dia, filaActual);
            }

            // Renderizar reservas si existen en este mes
            for (Reserva reserva : listaReservas) {
                // CORRECCIÓN CLAVE: Comparación por identificador (ID) y validación de nulidad
                if (reserva.getApartamento() != null && reserva.getApartamento().getId().equals(apto.getId())) {
                    Estancia estancia = reserva.getEstancia();

                    if (estancia != null && estancia.getFechaEntrada() != null &&
                            estancia.getFechaEntrada().getYear() == mesActual.getYear() &&
                            estancia.getFechaEntrada().getMonth() == mesActual.getMonth()) {

                        int colInicio = estancia.getFechaEntrada().getDayOfMonth();
                        int nochesSpan = (int) estancia.getNoches();

                        Label bloque = new Label(reserva.getEstado().toString());
                        bloque.setMaxWidth(Double.MAX_VALUE);
                        bloque.setMaxHeight(28.0);
                        bloque.setAlignment(javafx.geometry.Pos.CENTER);

                        bloque.getStyleClass().addAll("reserva-bloque", claseEstadoReserva(reserva.getEstado()));

                        gridCalendario.add(bloque, colInicio, filaActual);
                        GridPane.setColumnSpan(bloque, Math.max(1, nochesSpan));
                    }
                }
            }
        }
    }
}