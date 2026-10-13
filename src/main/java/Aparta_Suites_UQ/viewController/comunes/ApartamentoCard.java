package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ApartamentoCard {
    @FXML
    private ImageView img_apart;
    @FXML
    private VBox pane_estado;
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

    public void setApartamento(Apartamento apart){
        apartamentoActual=apart;
        lblCodigoCard.setText("Codigo: "+apart.getId());
        lblDormidorios.setText("Dormitorios: "+apart.getNumeroDormitorios());
        lblCapacidad.setText("Capacidad: "+apart.getCantMaximaPersonas());
        lblCaracteristicas.setText(apart.getCaracteristicas());
        lblPrecioNoche.setText("$"+apart.getPrecioNoche());
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
    }
    private Apartamento apartamentoActual;

    private Runnable onEliminarListener;
    public void setOnEliminarListener(Runnable listener) {
        this.onEliminarListener = listener;}
    @FXML
    private void eliminarApartamento(ActionEvent event) {
        System.out.println("Se hizo click");
        if (apartamentoActual != null) {
            System.out.println("Objeto que se elimina "+apartamentoActual.getNombre());
            boolean eliminado =Navegacion.getInstance().getListaApartamentos().remove(apartamentoActual);
            System.out.println("Se borro la lista?: "+eliminado);
            if (onEliminarListener != null) {
                onEliminarListener.run();
                System.out.println("Vista recargada correctamente.");
            }else {
                System.out.println("ADVERTENCIA: El listener de eliminación es nulo.");
        }
    }else {
            System.out.println("ERROR: apartamentoActual es null.");
        }
    }
    @FXML
    private void editarApartamento(ActionEvent event){
        if (apartamentoActual != null){
            Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(apartamentoActual);
            Navegacion.abrirModal(event, Paths.ADDAPARTAMENTO_PANEL);
            if (onEliminarListener !=null ){
                onEliminarListener.run();
            }
        }
    }
}
