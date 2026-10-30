package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;

public class ApartamentoFueraServicioController {
    @FXML
    private Button buttom_savedFServicio;

    @FXML
    private TextField txtMotivo;

    @FXML
    public void guardarMotivo(ActionEvent event){
        String motivo= txtMotivo.getText().trim();
    }
    public void asignarMotivo(Apartamento apartamento, String motivo){

    }
}
