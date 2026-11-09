package Aparta_Suites_UQ.viewController.comunes;

import Aparta_Suites_UQ.model.alojamiento.Apartamento;
import Aparta_Suites_UQ.model.alojamiento.EstadoOperativo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddApartamentoControllerTest {
    @Test
    void verficarValores(){
        Apartamento apartamento=new Apartamento("Cualquiera",-2,-3,"Baño",-3121, EstadoOperativo.OCUPADO);
        assertTrue(apartamento.getCantMaximaPersonas()>=0&&apartamento.getPrecioNoche()>=0.0&&apartamento.getNumeroDormitorios()>=0);
    }
}