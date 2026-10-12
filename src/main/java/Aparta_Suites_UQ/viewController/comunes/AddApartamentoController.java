package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import Aparta_Suites_UQ.utils.Navegacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddApartamentoController implements Initializable {
    @FXML
    private Button buttom_savedApart;

    @FXML
    private Button buttom_selectImg;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private TextField txtCapacidad;

    @FXML
    private TextField txtDormitorio;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecioNoche;
    @FXML
    private TextField txtCaracteristica;
    private Apartamento apartamentoCreado;
    @FXML
    void guardarApartamento(ActionEvent event) {
        String nombre=txtNombre.getText().trim();
        int dormitorios=Integer.parseInt(txtDormitorio.getText().trim());
        int capacidad=Integer.parseInt(txtCapacidad.getText().trim());
        String caracteristicas=txtCaracteristica.getText().trim();
        double precioNoche=Double.parseDouble(txtPrecioNoche.getText().trim());
        String valor = cbEstado.getValue();
        EstadoOperativo estadoOperativo = switch (valor) {
            case "Pendiente" -> EstadoOperativo.PENDIENTE;
            case "En preparacion" -> EstadoOperativo.EN_PREPARACION;
            case "Ocupado" -> EstadoOperativo.OCUPADO;
            case "Fuera de Servicio" -> EstadoOperativo.FUERA_DE_SERVICIO;
            case "Preparado" -> EstadoOperativo.PREPARADO;
            case null-> EstadoOperativo.FUERA_DE_SERVICIO;
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
            System.out.println("Apartamento actualizado con éxito.");
            Navegacion.getInstance().setApartamentoSeleccionadoParaEditar(null);

        } else {
            Apartamento nuevoApt = new Apartamento(nombre, dormitorios, capacidad, caracteristicas, precioNoche, estadoOperativo);
            Navegacion.getInstance().getListaApartamentos().add(nuevoApt);
            System.out.println("Se ha creado un nuevo apartamento.");
        }
        Stage stageActual = (Stage) buttom_savedApart.getScene().getWindow();
        stageActual.close();
    }
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
