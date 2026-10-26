package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.utils.Navegacion;
import Aparta_Suites_UQ.utils.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class AddApartamentoController implements Initializable {
    @FXML
    private AnchorPane contenedorPrincipal;
    @FXML
    private Button buttom_savedApart;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TextField txtCapacidad;

    @FXML
    private TextField txtDormitorio;
    @FXML
    private TextField txtMotivo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecioNoche;
    @FXML
    private TextField txtCaracteristica;
    @FXML
    private void registrarMotivo(ActionEvent event){
            if (cbEstado.getValue().equals("Fuera de Servicio")){ txtMotivo.setVisible(true);
            }else {
                txtMotivo.setVisible(false);
            }
    }
    @FXML
    void guardarApartamento(ActionEvent event) {
        String nombre=txtNombre.getText().trim();
        int dormitorios;
        if (Integer.parseInt(txtDormitorio.getText().trim())>=0){
            dormitorios=Integer.parseInt(txtDormitorio.getText().trim());
        }else {
            Navegacion.enviarAlertaWarning("Se debe enviar valores positivos");
            return;
        }
        int capacidad=0;
        if (Integer.parseInt(txtCapacidad.getText().trim())>=0){
            capacidad=Integer.parseInt(txtCapacidad.getText().trim());
        }else {
            Navegacion.enviarAlertaWarning("Se debe enviar valores positivos");
            return;
        }
        String caracteristicas=txtCaracteristica.getText().trim();
        double precioNoche=0.0;
        if (Double.parseDouble(txtPrecioNoche.getText().trim())>=0.0){
            precioNoche=Double.parseDouble(txtPrecioNoche.getText().trim());
        }else {
            Navegacion.enviarAlertaWarning("Se debe enviar valores positivos");
        }
        String valor = cbEstado.getValue();
        EstadoOperativo estadoOperativo = switch (valor) {
            case "Pendiente" -> EstadoOperativo.PENDIENTE;
            case "En preparacion" -> EstadoOperativo.EN_PREPARACION;
            case "Ocupado" -> EstadoOperativo.OCUPADO;
            case "Fuera de Servicio" ->EstadoOperativo.FUERA_DE_SERVICIO;
            case "Preparado" -> EstadoOperativo.PREPARADO;
            case null-> EstadoOperativo.PENDIENTE;
            default -> throw new IllegalArgumentException("Estado no válido o no seleccionado");
        };
        Apartamento aptEdicion = Navegacion.getInstance().getApartamentoSeleccionadoParaEditar();
        if (aptEdicion != null) {
            // Modo edicion
            aptEdicion.setNumeroDormitorios(dormitorios);
            aptEdicion.setCantMaximaPersonas(capacidad);
            aptEdicion.setCaracteristicas(caracteristicas);
            aptEdicion.setPrecioNoche(precioNoche);
            aptEdicion.setEstadoOperativo(estadoOperativo);
            try {
                if (cbEstado.getValue().equals("Fuera de Servicio")) {
                    if (txtMotivo.getText().isEmpty()){
                        Navegacion.enviarAlertaWarning("Escribe el motivo del fuera de servicio");
                        return;
                    }
                    aptEdicion.setMotivoFueraServicio(txtMotivo.getText().trim());
                }else {aptEdicion.setMotivoFueraServicio(null);}
            }catch (NullPointerException e){
            }
            Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(null);

        } else {
            this.apartamentoActual= new Apartamento(nombre, dormitorios, capacidad, caracteristicas, precioNoche, estadoOperativo);
            if (txtMotivo!=null) {apartamentoActual.setMotivoFueraServicio(txtMotivo.getText().trim());
            }
            Navegacion.getInstance().getListaApartamentos().add(apartamentoActual);
        }
        Stage stageActual = (Stage) buttom_savedApart.getScene().getWindow();
        stageActual.close();
    }
    private Apartamento apartamentoActual;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                "Pendiente", "Preparado", "Ocupado", "Fuera de Servicio", "En preparacion"
        );
        cbEstado.setItems(opciones);
        Apartamento aptAEditar = Navegacion.getInstance().getApartamentoSeleccionadoParaEditar();

        if (aptAEditar != null) {
            txtNombre.setText(aptAEditar.getNombre());
            txtDormitorio.setText(String.valueOf(aptAEditar.getNumeroDormitorios()));
            txtCapacidad.setText(String.valueOf(aptAEditar.getCantMaximaPersonas()));
            txtCaracteristica.setText(aptAEditar.getCaracteristicas());
            txtPrecioNoche.setText(String.valueOf(aptAEditar.getPrecioNoche()));
        }
    }
}
