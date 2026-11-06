package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;

public class ApartamentoCard {
    @FXML
    private ImageView img_apart;
    @FXML
    private VBox pane_estado;
    @FXML
    private Label lbl_motivoFueraServicio;
    @FXML
    private Label lbl_motivoFueraServicio1;
    @FXML
    private Button buttom_editar;

    @FXML
    private Button buttom_eliminar;

    @FXML
    private Button buttom_visualizar;

    @FXML
    private Label lblCapacidad;

    @FXML
    private Label lblCaracteristicas;

    @FXML
    private Label lblCodigoCard;

    @FXML
    private Label lblDormidorios;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblPrecioNoche;
    private boolean modoSoloEstado = false;
    private Apartamento apartamentoActual;
    private Runnable onEliminarListener;
    private Runnable onActualizarListener;
    public void setOnEliminarListener(Runnable listener) {
        this.onEliminarListener = listener;
    }
    public void setOnActualizarListener(Runnable listener) {
        this.onActualizarListener = listener;
    }

    public void setApartamento(Apartamento apart){
        apartamentoActual = apart;
        lblCodigoCard.setText("Codigo: " + apart.getId());
        lblDormidorios.setText("Dormitorios: " + apart.getNumeroDormitorios());
        lblCapacidad.setText("Capacidad: " + apart.getCantMaximaPersonas());
        lblCaracteristicas.setText(apart.getCaracteristicas());
        lblPrecioNoche.setText("$" + apart.getPrecioNoche());

        // Limpiar clases previas para evitar acumulación al refrescar
        pane_estado.getStyleClass().removeAll(
                "estadoOperativoPreparado",
                "estadoOperativoPendiente",
                "estadoOperativoEn_preparacion",
                "estadoOperativoOcupado",
                "estadoOperativoFuera_De_Servicio"
        );

        switch (apart.getEstadoOperativo()){
            case PREPARADO :
                lblEstado.setText("Preparado");
                pane_estado.getStyleClass().add("estadoOperativoPreparado");
                break;
            case PENDIENTE:
                lblEstado.setText("Pendiente");
                pane_estado.getStyleClass().add("estadoOperativoPendiente");
                break;
            case EN_PREPARACION:
                lblEstado.setText("En preparacion");
                pane_estado.getStyleClass().add("estadoOperativoEn_preparacion");
                break;
            case OCUPADO:
                lblEstado.setText("Ocupado");
                lblEstado.setStyle("-fx-text-fill:#fff");
                pane_estado.getStyleClass().add("estadoOperativoOcupado");
                break;
            case FUERA_DE_SERVICIO:
                lblEstado.setText("Fuera de servicio");
                pane_estado.getStyleClass().add("estadoOperativoFuera_De_Servicio");
                break;
        }

        if (apart.getMotivoFueraServicio() != null && apart.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO){
            lbl_motivoFueraServicio.setText(apart.getMotivoFueraServicio());
            lbl_motivoFueraServicio.setVisible(true);
            lbl_motivoFueraServicio1.setVisible(true);
        } else {
            lbl_motivoFueraServicio1.setVisible(false);
            lbl_motivoFueraServicio.setVisible(false);
        }
    }
    @FXML
    private void eliminarApartamento(ActionEvent event) {
        System.out.println("Se hizo click");
        if (apartamentoActual != null) {
            System.out.println("Objeto que se elimina " + apartamentoActual.getNombre());
            boolean eliminado = Navegacion.getInstance().getListaApartamentos().remove(apartamentoActual);
            System.out.println("Se borro la lista?: " + eliminado);
            if (onEliminarListener != null) {
                onEliminarListener.run();
                System.out.println("Vista recargada correctamente.");
            } else {
                System.out.println("ADVERTENCIA: El listener de eliminación es nulo.");
            }
        } else {
            System.out.println("ERROR: apartamentoActual es null.");
        }
    }
    public void setModoSoloEstado(boolean modoSoloEstado) {
        this.modoSoloEstado = modoSoloEstado;

        // Ocultar el botón de eliminar si estamos en modo solo estado
        if (buttom_eliminar != null) {
            buttom_eliminar.setVisible(!modoSoloEstado);

            // Opcional: quita el espacio reservado del botón en el layout si es necesario
            buttom_eliminar.setManaged(!modoSoloEstado);
        }
    }
    @FXML
    private void editarApartamento(ActionEvent event) {
        if (apartamentoActual != null) {
            Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(apartamentoActual);

            if (modoSoloEstado) {
                // Abre el modal de estado
                Navegacion.abrirModal(event, Paths.EDITAR_ESTADO_APART);
            } else {
                // Abre el modal completo
                Navegacion.abrirModal(event, Paths.ADDAPARTAMENTO_PANEL);
            }

            // Se ejecuta cuando el modal se cierra
            if (onActualizarListener != null) {
                onActualizarListener.run();
            } else if (onEliminarListener != null) {
                onEliminarListener.run();
            }
        }
    }
}