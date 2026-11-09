package Aparta_Suites_UQ.controller.comunes;

import Aparta_Suites_UQ.controller.Acceso;
import Aparta_Suites_UQ.controller.servicio.AutenticacionServicio;

public class LoginController {

    private final AutenticacionServicio autenticacionServicio = AutenticacionServicio.getInstance();

    public Acceso autenticar(String usuario, String password) {
        return autenticacionServicio.autenticarLogin(usuario, password);
    }
}
