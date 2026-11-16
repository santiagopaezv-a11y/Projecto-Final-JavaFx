package Aparta_Suites_UQ.controller.user;

import Aparta_Suites_UQ.controller.Acceso;
import Aparta_Suites_UQ.controller.servicio.AutenticacionServicio;

/**
 * Controller de la pantalla del portal de usuario (cambio de módulo). Su vista es InterfazUsuarioViewController.
 */
public class InterfazUsuarioController {
    private final AutenticacionServicio autenticacionServicio = AutenticacionServicio.getInstance();
    public Acceso autenticarModulo(String usuario, String password) {
        return autenticacionServicio.autenticarPortal(usuario, password);
    }
}
